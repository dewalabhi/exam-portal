package com.programmers.model.converter;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(

        componentModel = "spring",
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
        implementationPackage = "<PACKAGE_NAME>.generated",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        // don't complain about missing fields.
        // for example going from an abstract audit entity to a dto.
        // The lack of the auditing fields in the dto is not an error.
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        // no fuzzy type conversions
        typeConversionPolicy = ReportingPolicy.ERROR
)

public interface MapStructConfiguration {


}
