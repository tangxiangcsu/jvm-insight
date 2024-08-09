package com.jvminsight.jvmprofiler.transformer;

import com.jvminsight.jvmprofiler.dto.ClassAndMethod;
import com.jvminsight.jvmprofiler.dto.ClassMethodArgument;
import com.jvminsight.jvmprofiler.utils.ClassAndMethodFilter;
import com.jvminsight.jvmprofiler.utils.ClassMethodArgumentFilter;
import javassist.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.transformer
 * @NAME: JavaAgentFileTransformer
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class JavaAgentFileTransformer implements ClassFileTransformer {

    private ClassAndMethodFilter durationProfilingFilter;
    private ClassMethodArgumentFilter argumentProfilingFilter;

    public JavaAgentFileTransformer(List<ClassAndMethod> durationProfiling, List<ClassMethodArgument> argumentProfiling){
        this.durationProfilingFilter = new ClassAndMethodFilter(durationProfiling);
        this.argumentProfilingFilter = new ClassMethodArgumentFilter(argumentProfiling);
    }
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try{
            if(className == null ||className.isEmpty()){
                log.error("empty class name");
                return null;
            }
            return transformeImpl(loader, className, classfileBuffer);
        }catch (Exception e){
            log.warn("Failed to transform class" + className, e);
            return classfileBuffer;
        }
    }
    private byte[] transformeImpl(ClassLoader loader, String className, byte[] classfileBuffer){
        if(durationProfilingFilter==null&&argumentProfilingFilter==null){
            return null;
        }
        String normalizedClassName = className.replaceAll("/", ".");
        log.info("className transform to: " + normalizedClassName);
        // 过滤掉未见听的类
        if(!durationProfilingFilter.matchClass(className)&&!argumentProfilingFilter.matchClass(className)){
            return null;
        }
        byte[] byteCode;
        log.info("start transforming class : "+ className);
        try{
            ClassPool classPool = new ClassPool();
            classPool.appendClassPath(new LoaderClassPath(loader));
            final CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

            CtMethod[] ctMethods = ctClass.getDeclaredMethods();
            for(CtMethod ctMethod : ctMethods){
                boolean enableClassAndMethodProfiler = durationProfilingFilter.matchMethod(ctClass.getName(), ctMethod.getName());
                List<Integer> enableArgumentProfiler = argumentProfilingFilter.matchMethod(ctClass.getName(), ctMethod.getName());

            }

            byteCode = ctClass.toBytecode();
            ctClass.detach();


        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
            byteCode = null;
        }
        return byteCode;
    }
    private void transformMethod(String normalizedClassName, CtMethod method, boolean enableClassAndMethodProfiler, List<Integer> enableArgumentProfiler) {
        if(method.isEmpty()){
            log.info("Ignored empty class method : "+ method.getLongName());
            return;
        }

        if(!enableClassAndMethodProfiler&&!enableArgumentProfiler.isEmpty()){
            return;
        }

        try{
            if(enableClassAndMethodProfiler){
                method.addLocalVariable("startTime_java_agent_instrument", CtClass.longType);
                method.addLocalVariable("durationTime_java_agent_instrument", CtClass.longType);

            }

            StringBuilder sb = new StringBuilder();
            sb.append("{");

            /**
             * 设置Agent构造开始时间
             */
            if(enableClassAndMethodProfiler){
                sb.append("startTime_java_agent_instrument = System.currentTimeMillis();");
            }

            for(Integer argument : enableArgumentProfiler){
                if(argument >=1){
                    sb.append(String.format("try{collectMethodArgument(\"%s\", \"%s\", %s, String.valueOf($%s));}catch(Exception e){e.printStackTrace();}",
                            normalizedClassName,
                            method.getName(),
                            argument,
                            argument));
                }else{
                    sb.append(String.format("try{collectMethodArgument(\"%s\", \"%s\", %s, \"\");}catch(Throwable ex){ex.printStackTrace();}",
                            normalizedClassName,
                            method.getName(),
                            argument,
                            argument));
                }
            }
            sb.append("}");

            /**
             * 执行方法前插入，可能是持续时间
             */
            method.insertBefore(sb.toString());

            if(enableClassAndMethodProfiler){
                method.insertAfter("{" +
                        "durationTime_java_agent_instrument = System.currentTimeMillis() - startTime_java_agent_instrument;" +
                        String.format("collectMethodDuration(\"%s\", \"%s\", durationTime_java_agent_instrument);}catch(Exception ex){ex.printStackTrace();}", normalizedClassName, method.getName()) +
                        "}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Failed to transform class method: " + method.getLongName(), e);
        }

    }
}
