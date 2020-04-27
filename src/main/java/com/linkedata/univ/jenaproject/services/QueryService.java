package com.linkedata.univ.jenaproject.services;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.sparql.core.Quad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Class contains methods to allow
 * for easier querying. Encapsulates creation of
 * objects necessary for querying.
 */
public class QueryService {
    private InfModel infModel;
    private Logger LOG = LoggerFactory.getLogger(QueryService.class);

    /**
     * Method used to build QueryService object
     * @param filepath
     * @return Query Service
     */
    public static QueryService buildOwlQueryService(String filepath) {
        QueryService queryService = new QueryService();
        Model model = ModelFactory.createOntologyModel();
        try {
            var file = new ClassPathResource("univ.ttl").getFile().getPath();
            model.read(file);
        } catch (Exception ex) {
            System.err.println("welp this sucks: " + ex);
        }

        Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
        reasoner = reasoner.bindSchema(model);
        InfModel owlSchema = ModelFactory.createInfModel(reasoner, model);

        queryService.setInfModel(owlSchema);

        return queryService;
    }

    private QueryService() {
        // private constructor to be used by builder
    }

    public String selectQuery(QueryObject queryObject) {
        return selectQuery(getQueryExecution(this.infModel, queryObject.queryString), queryObject.message);
    }

    public void constructQuery(QueryObject queryObject) {
        constructQuery(getQueryExecution(this.infModel, queryObject.queryString), queryObject.message);
    }

    public void askQuery(QueryObject queryObject) {
        askQuery(getQueryExecution(this.infModel, queryObject.queryString), queryObject.message);
    }

    public void describeQuery(QueryObject queryObject) {
        describeQuery(getQueryExecution(this.infModel, queryObject.queryString), queryObject.message);
    }

    public void printSchema() {
        infModel.write(System.out, "TTL");
    }

    /**
     * Method will execute a select query and print out the resultSet
     * @param queryExecution - object used to execute the select query
     * @param question - question to print to console
     * @return response - JSON representation of response
     */
    private String selectQuery(QueryExecution queryExecution, String question) {
        String response = "";
        try {
            ResultSet resultSet = queryExecution.execSelect();
            LOG.info(question);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ResultSetFormatter.outputAsJSON(byteArrayOutputStream, resultSet);
            response = new String(byteArrayOutputStream.toByteArray());
            LOG.info(response);

        } finally {
            queryExecution.close();
        }
        return response;
    }

    /**
     * Executes construct query and prints out the new graph
     * @param queryExecution
     * @param message
     */
    private void constructQuery(QueryExecution queryExecution, String message) {
        System.out.println(message);
        try {
            Iterator<Quad> triples = queryExecution.execConstructQuads();
            while (triples.hasNext()) {
                Quad quad = triples.next();
                System.out.println(quad.getSubject() + " " + quad.getPredicate() + " " + quad.getObject());
            }
        } finally {
            queryExecution.close();
        }
        System.out.println();
    }

    private void askQuery(QueryExecution queryExecution, String message) {
        System.out.println(message);
        try {
            System.out.println(queryExecution.execAsk() ? "Yes" : "Nope");
        } finally {
            queryExecution.close();
        }
    }

    private void describeQuery(QueryExecution queryExecution, String message) {
        System.out.println(message);
        try {
            queryExecution.execDescribe().write(System.out, "TTL");
        } finally {
            queryExecution.close();
        }
    }

    /**
     *
     * @param owlSchema
     * @param queryString
     * @return returns the QueryExecution to be used
     */
    private QueryExecution getQueryExecution(InfModel owlSchema, String queryString) {
        return QueryExecutionFactory.create(QueryFactory.create(queryString), owlSchema);
    }

    private String getFileInputStream(String fileName) {
        var filePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + fileName;
        LOG.info("File Path: " + filePath);
        return filePath;
    }

    private void setInfModel(InfModel infModel) {
        this.infModel = infModel;
    }
}
