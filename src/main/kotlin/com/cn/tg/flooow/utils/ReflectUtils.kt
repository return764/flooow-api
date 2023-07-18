package com.cn.tg.flooow.utils

import java.lang.reflect.Field

object ReflectUtils {
    fun getDeclaredFieldWithAnnotation(clazz: Class<*>, annotation: Class<out Annotation>): List<Field> {
        return clazz.declaredFields.filter {
            it.isAnnotationPresent(annotation)
        }
    }
}
