package com.linkedata.univ.jenaproject.controllers;

import com.linkedata.univ.jenaproject.services.QueryObject;
import com.linkedata.univ.jenaproject.services.QueryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import javax.print.attribute.standard.Media;

@RestController()
@RequestMapping("/select")
public class SelectController {
    private QueryService queryService;
    private String prefix = "PREFIX : <http://www.cs.ccsu.edu/SrilakshmiDoma/University.owl#>" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";

    public SelectController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testQuery() {
        QueryObject queryObject = new QueryObject("Who are the students in the university?",
                                                prefix + " SELECT ?First_Name (COUNT(?C) AS ?Course_Count)\n" +
                                                        "WHERE\n" +
                                                        "{    ?Lecture rdf:type     :Teaching_Faculty;\n" +
                                                        "                      :teaches ?C;\n" +
                                                        "                :first_Name ?First_Name.\n" +
                                                        "}GROUP BY ?First_Name\n" +
                                                        " ORDER BY ?First_Name\n",
                                                QueryObject.QueryType.SELECT);
        return queryService.selectQuery(queryObject);
    }

    @GetMapping(value = "/student-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentDetails(@RequestParam() String firstName,
                                 @RequestParam() String lastName) {
        QueryObject queryObject = new QueryObject("What are " + firstName + " student details",
                                                    prefix + "SELECT ?phone ?email ?address ?gender ?age ?studentId ?advisor ?enrolled ?admissionYear ?graduationYear " +
                                                            "WHERE " +
                                                            "{ " +
                                                            "?student a :Student; " +
                                                            ":first_Name '" + firstName +"'; " +
                                                            ":last_Name '" + lastName + "'; " +
                                                            ":phone ?phone; " +
                                                            ":Email_Id ?email; " +
                                                            ":age ?age; " +
                                                            ":address ?address; " +
                                                            ":Gender ?gender; " +
                                                            ":student_Id ?studentId; " +
                                                            ":Advisor ?advisor; " +
                                                            ":has_Enrolled_In ?enrolled; " +
                                                            ":Year_Of_Admission ?admissionYear;"  +
                                                            ":Year_Of_Graduation ?graduationYear" +
                                                            "}", QueryObject.QueryType.SELECT);
        return queryService.selectQuery(queryObject);
    }

    @GetMapping(value = "/course-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public String courseDetails(@RequestParam() String courseId) {
        var course = ":" + courseId;
        QueryObject queryObject = new QueryObject("What are the " + course + "details?",
                                                    prefix + "SELECT ?title ?prerequisite ?lecturerEmail ?book " +
                                                            "WHERE { " +
                                                             course + " a :Courses ;" +
                                                                        ":title ?title .\n" +
                                                            " OPTIONAL {" +
                                                             course + " :is_Offered_By ?lecturer .\n" +
                                                            "?lecturer :Email_Id ?lecturerEmail } " +
                                                            "OPTIONAL { " + course + " :uses ?textBook.\n" +
                                                            "?textBook :title ?book }" +
                                                            "OPTIONAL { " + course + " :has_Prerequisite ?prerequisite} }",
                                                    QueryObject.QueryType.SELECT);
        System.out.println("QUERY STRING " + queryObject.toString());
        return queryService.selectQuery(queryObject);
    }


    @GetMapping(value = "/faculty-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public String facultyDetails(@RequestParam() String lastName) {
        QueryObject queryObject = new QueryObject("Professor " + lastName + "Teaches?",
                prefix + "SELECT ?name " +
                        "WHERE { " +
                        "?Lecture rdf:type     :Teaching_Faculty;" +
                        " :teaches ?C; " +
                        ":last_Name '" + lastName + "' ." +
                        "?C :title ?name.}",
                QueryObject.QueryType.SELECT);
        return queryService.selectQuery(queryObject);
    }

    @GetMapping(value = "/book-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public String bookDetails(@RequestParam() String bookName) {
        QueryObject queryObject = new QueryObject("What are " + bookName + "details?",
                prefix + "SELECT ?title ?author ?ISBN ?class " +
                        "WHERE { "+
                        "?Book rdf:type :Books; " +
                        ":title '" + bookName + "';" +
                        ":title ?title;" +
                        ":author ?author;" +
                        ":ISBN ?ISBN. " +
                        "OPTIONAL{ ?Book :is_Used_By ?course }" +
                        "OPTIONAL{ ?course :title ?class}}",
                QueryObject.QueryType.SELECT);
        return queryService.selectQuery(queryObject);
    }

    @GetMapping(value = "/student-events", produces = MediaType.APPLICATION_JSON_VALUE)
    public String eventsVisited(@RequestParam() String firstName,
                                 @RequestParam() String lastName) {
        QueryObject queryObject = new QueryObject("What events did  " + firstName + " visit?",
                prefix + "SELECT ?Attends " +
                        "WHERE " +
                        "{ " +
                        "?student a :Student; " +
                        ":first_Name '" + firstName +"'; " +
                        ":last_Name '" + lastName + "'; " +
                        ":has_Attends ?Attends. " +
                        "}", QueryObject.QueryType.SELECT);
        return queryService.selectQuery(queryObject);
    }

    @GetMapping(value = "/student-graduating", produces = MediaType.APPLICATION_JSON_VALUE)
    public String studentGraduating(@RequestParam() int year) {
        QueryObject queryObject = new QueryObject("What students are graduating in" +year + " ? ",
                prefix + "SELECT ?firstName ?lastName ?studentId ?admissionYear " +
                        "WHERE " +
                        "{ " +
                        "?student a :Student; " +
                        ":first_Name  ?firstName; " +
                        ":last_Name  ?lastName; " +
                        ":student_Id ?studentId; " +
                        ":Year_Of_Graduation " + year+"; " +
                       ":Year_Of_Admission ?admissionYear"  +
                        "}", QueryObject.QueryType.SELECT);
        return queryService.selectQuery(queryObject);
    }
}
