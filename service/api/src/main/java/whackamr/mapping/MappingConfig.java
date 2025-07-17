package whackamr.mapping;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class MappingConfig {
    private static final Logger logger = LoggerFactory.getLogger(MappingConfig.class);
    private List<PropertyMap<?,?>> propertyMaps;

    @Bean
    ModelMapper modelMapper()
    {
        logger.info("Creating model mapper");
        logger.info("found {} propertyMaps", propertyMaps.size());

        var mapper = new ModelMapper();

        propertyMaps.forEach(mapper::addMappings);

        return mapper;
    }
}