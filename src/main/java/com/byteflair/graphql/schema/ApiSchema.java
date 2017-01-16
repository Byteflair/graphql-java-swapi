package com.byteflair.graphql.schema;

import com.byteflair.graphql.datafetcher.GenericArgumentIdDataFetcher;
import com.byteflair.graphql.datafetcher.GenericConnectionDataFetcher;
import com.byteflair.graphql.schema.type.*;
import graphql.relay.Relay;
import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static graphql.Scalars.GraphQLInt;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLSchema.newSchema;

/**
 * Created by calata on 12/01/17.
 */
@Component
public class ApiSchema {

    @Autowired
    private PeopleType peopleType;
    @Autowired
    private FilmType filmType;
    @Autowired
    private PlanetType planetType;

    @Autowired
    private GenericArgumentIdDataFetcher peopleDataFetcher;
    @Autowired
    private GenericArgumentIdDataFetcher filmDataFetcher;
    @Autowired
    private GenericArgumentIdDataFetcher planetDataFetcher;

    @Autowired
    private GenericConnectionDataFetcher filmsConnectionDataFetcher;
    @Autowired
    private GenericConnectionDataFetcher peopleConnectionDataFetcher;
    @Autowired
    private GenericConnectionDataFetcher planetConnectionDataFetcher;

    /**
     * Build schema with query type and dictionary
     * Mutation type is not required
     * @return
     */
    public GraphQLSchema buildSchema(){

        return newSchema()
            .query(buildQuery())
            .build(buildDictionary());
    }

    /**
     * Build dictionary with all model defined types
     * @return
     */
    private Set<GraphQLType> buildDictionary(){
        Set<GraphQLType> dictionary =  new HashSet<>();

        dictionary.add(filmType.buildFilmType());
        dictionary.add(planetType.buildPlanetType());
        dictionary.add(peopleType.buildPeopleType());

        dictionary.add(new FilmConnection().buildFilmConnection());
        dictionary.add(new PlanetConnection().buildPlanetConnection());
        dictionary.add(new PeopleConnection().buildPeopleConnection());

        return dictionary;
    }

    /**
     * Build Query Type
     * @return
     */
    private GraphQLObjectType buildQuery(){
        Relay relay = new Relay();

        // Each service instance must define relative URI
        filmDataFetcher.setURI("/films/");
        peopleDataFetcher.setURI("/people/");
        planetDataFetcher.setURI("/planets/");

        filmsConnectionDataFetcher.setURI("/films/");
        peopleConnectionDataFetcher.setURI("/people/");
        planetConnectionDataFetcher.setURI("/planets/");

        return newObject()
            .name("Query")
            .description("Root query")
            .field(newFieldDefinition()
                       .name("film")
                       .description("Find a film by id")
                       .argument(newArgument() // argumento (id: ID)
                                   .name("id")
                                   .description("Film ID")
                                   .type(new GraphQLNonNull(GraphQLInt)))
                       .type(new GraphQLTypeReference("Film"))
                       .dataFetcher(filmDataFetcher))
            .field(newFieldDefinition()
                       .name("planet")
                       .description("Find a planet by id")
                       .argument(newArgument() // argumento (id: ID)
                                   .name("id")
                                   .description("Planet ID")
                                   .type(new GraphQLNonNull(GraphQLInt)))
                       .type(new GraphQLTypeReference("Planet"))
                       .dataFetcher(planetDataFetcher))
            .field(newFieldDefinition()
                       .name("people")
                       .description("Find a character by id")
                       .argument(newArgument() // argumento (id: ID)
                                   .name("id")
                                   .description("People ID")
                                   .type(new GraphQLNonNull(GraphQLInt)))
                       .type(new GraphQLTypeReference("People"))
                       .dataFetcher(peopleDataFetcher))
            .field(newFieldDefinition()
                       .name("allFilms")
                       .description("Retrieves all films")
                       .type(new GraphQLTypeReference("FilmConnection"))
                       .argument(relay.getConnectionFieldArguments())
                       .dataFetcher(filmsConnectionDataFetcher))
            .field(newFieldDefinition()
                       .name("allPlanets")
                       .description("Retrieves all planets")
                       .type(new GraphQLTypeReference("PlanetConnection"))
                       .argument(relay.getConnectionFieldArguments())
                       .dataFetcher(planetConnectionDataFetcher))
            .field(newFieldDefinition()
                       .name("allPeople")
                       .description("Retrieves all characers")
                       .type(new GraphQLTypeReference("PeopleConnection"))
                       .argument(relay.getConnectionFieldArguments())
                       .dataFetcher(peopleConnectionDataFetcher))

            .build();
    }

}
