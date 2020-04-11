package com.linkedata.univ.jenaproject.controllers;

import com.linkedata.univ.jenaproject.services.QueryObject;
import com.linkedata.univ.jenaproject.services.QueryService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/select")
public class SelectController {
    private QueryService queryService;

    private static String PREFIX = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
            "PREFIX univ: <http://www.cs.ccsu.edu/~neli/university.owl#>" +
            "PREFIX vcard: <http://www.w3.org/vcard/ns#>" +
            "PREFIX ex: <http://example.org/>";

    public SelectController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping(value = "/query")
    public String testQuery() {
        String prefix = "PREFIX : <http://www.cs.ccsu.edu/SrilakshmiDoma/University.owl#>" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";

        QueryObject queryObject = new QueryObject("Who are the students in the university?",
                                                prefix + " SELECT ?First_Name (COUNT(?C) AS ?Course_Count)\n" +
                                                        "WHERE\n" +
                                                        "{    ?Lecture rdf:type     :Teaching_Faculty;\n" +
                                                        "                      :teaches ?C;\n" +
                                                        "                :first_Name ?First_Name.\n" +
                                                        "}GROUP BY ?First_Name\n" +
                                                        " ORDER BY ?First_Name\n",
                                                QueryObject.QueryType.SELECT);

        queryService.selectQuery(queryObject);
        return "did query";
    }
}
