package com.byteflair.graphql.schema.type;

import com.byteflair.graphql.datafetcher.GenericEmbeddedDataFetcher;
import graphql.Scalars;
import graphql.relay.Relay;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

/**
 * Created by calata on 12/01/17.
 */
@Component
public class PeopleType {

    @Autowired
    private GenericEmbeddedDataFetcher homeworldEmbeddedDataFetcher;
    @Autowired
    private GenericEmbeddedDataFetcher filmsEmbeddedDataFetcher;

    public GraphQLObjectType buildPeopleType(){

        // Each GenericEmbeddedDataFetcher service must define its relationshipName and connection variables
        homeworldEmbeddedDataFetcher.setRelationshipName("homeworld");
        filmsEmbeddedDataFetcher.setRelationshipName("films");
        filmsEmbeddedDataFetcher.setConnection(Boolean.TRUE);

        Relay relayConnection = new Relay();

        return newObject()
            .name("People")
            .description("People - Represents an SW character")
            .field(newFieldDefinition()
                       .name("birth_year")
                       .description("Date of birth")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("gender")
                       .description("Gender of the character")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("hair_color")
                       .description("Hair color of the character")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("height")
                       .description("Height of the character (cm)")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("mass")
                       .description("Weight of the characters (kg)")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("name")
                       .description("Name of the character")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("skin_color")
                       .description("Skin color of the character")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("created")
                       .description("API insertion date")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("edited")
                       .description("API last modification date")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("url")
                       .description("URI of the element")
                       .type(Scalars.GraphQLString)
                       .build())
            .field(newFieldDefinition()
                       .name("homeworld")
                       .description("Planet where character took birth")
                       .type(new GraphQLTypeReference("Planet")) // Will look into schema for Planet type
                       .dataFetcher(homeworldEmbeddedDataFetcher) // Planet (homeworld) relationship DataFetcher
                       .build())
            .field(newFieldDefinition()
                       .name("filmsConnection")
                       .description("Films which character appears")
                       .type(new GraphQLTypeReference("FilmConnection"))
                       .argument(relayConnection.getConnectionFieldArguments())
                       .dataFetcher(filmsEmbeddedDataFetcher)
                       .build())
            .build();
    }
}
