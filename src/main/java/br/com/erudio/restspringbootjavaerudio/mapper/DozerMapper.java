package br.com.erudio.restspringbootjavaerudio.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class DozerMapper {

    public static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <Origem, Destino> Destino parseObject(Origem origin, Class<Destino> destination) {
        return mapper.map(origin, destination);
    }

    public static <Origem, Destino> List<Destino> parseListObjects(List<Origem> origin, Class<Destino> destination) {
        List<Destino> destinationObjects = new ArrayList<Destino>();
        for (Origem o: origin) {
            destinationObjects.add(mapper.map(o, destination));
        }
        return destinationObjects;
    }

}
