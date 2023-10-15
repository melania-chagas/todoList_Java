package com.example.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

    // Método para copiar propriedades não nulas de um objeto de origem (source) para um objeto de destino (target)
    public static void copyNonNullProperties(Object source, Object target) {
        // Utiliza a classe BeanUtils do Spring para copiar as propriedades não nulas
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    // Método para obter os nomes das propriedades nulas de um objeto (source)
    public static String[] getNullPropertyNames(Object source) {
        // Cria um wrapper (invólucro) para o objeto source usando BeanWrapperImpl
        final BeanWrapper src = new BeanWrapperImpl(source);

        // Obtém todas as propriedades do objeto source usando PropertyDescriptor
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        // Cria um conjunto (Set) para armazenar os nomes das propriedades nulas
        Set<String> emptyNames = new HashSet<>();

        // Itera sobre todas as propriedades do objeto source
        for(PropertyDescriptor pd: pds) {
            // Obtém o valor da propriedade atual
            Object srcValue = src.getPropertyValue(pd.getName());

            // Verifica se o valor da propriedade é nulo
            if(srcValue == null) {
                // Se for nulo, adiciona o nome da propriedade ao conjunto emptyNames
                emptyNames.add(pd.getName());
            }
        }

        // Converte o conjunto emptyNames em um array de strings
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
