/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import dao.MedicoJpaController;
import dto.Medico;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.BcryptJava;

/**
 *
 * @author yello
 */
@WebServlet(name = "MedicoCrud", urlPatterns = {"/medico"})
public class MedicoCrud extends HttpServlet {

    MedicoJpaController mediDAO = new MedicoJpaController();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        List<Medico> alumnList = mediDAO.findMedicoEntities();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Medico alumno : alumnList) {
            JsonObjectBuilder clieJson = Json.createObjectBuilder()
                    .add("codigo", alumno.getCodiMedi())
                    .add("nombre", alumno.getNombMedi())
                    .add("appa", alumno.getAppMedi())
                    .add("apma", alumno.getApmaMedi())
                    .add("ndni", alumno.getNdniMedi())
                    .add("fechnaci", alumno.getFechNaciMedi().toString())
                    .add("username", alumno.getLogiMedi());
            jsonArrayBuilder.add(clieJson);
        }
        String data = "{\"data\":" + jsonArrayBuilder.build().toString() + "}";
        out.print(data);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            JsonReader jsonReader = Json.createReader(request.getReader());
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();

            String nombre = jsonObject.getString("nombre");
            String appa = jsonObject.getString("appa");
            String apma = jsonObject.getString("apma");
            String ndni = jsonObject.getString("ndni");
            String username = jsonObject.getString("username");
            String fechnaci = jsonObject.getString("fechnaci");
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formato.parse(fechnaci);

            //Password
            String passHashed = BcryptJava.hashPassword(jsonObject.getString("password"));

            Medico alumno = new Medico(ndni, appa, apma, nombre, fecha, username, passHashed);
            mediDAO.create(alumno);
            JsonObject jsonResponse = Json.createObjectBuilder()
                    .add("success", true)
                    .build();
            out.print(jsonResponse.toString());
            out.flush();

        } catch (ParseException ex) {

        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            JsonReader jsonReader = Json.createReader(request.getReader());
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();

            int codigo = jsonObject.getInt("codigo");
            String nombre = jsonObject.getString("nombre");
            String appa = jsonObject.getString("appa");
            String apma = jsonObject.getString("apma");
            String ndni = jsonObject.getString("ndni");
            String username = jsonObject.getString("username");
            String fechnaci = jsonObject.getString("fechnaci");
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formato.parse(fechnaci);

            JsonObject jsonResponse;
            Medico alumno = mediDAO.findMedico(codigo);
            alumno.setNombMedi(nombre);
            alumno.setAppMedi(appa);
            alumno.setApmaMedi(apma);
            alumno.setNdniMedi(ndni);
            alumno.setLogiMedi(username);
            alumno.setFechNaciMedi(fecha);

            mediDAO.edit(alumno);

            jsonResponse = Json.createObjectBuilder()
                    .add("success", true)
                    .build();
            out.print(jsonResponse.toString());
            out.flush();

        } catch (ParseException ex) {
            
        } catch (Exception ex) {
            
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonReader jsonReader = Json.createReader(request.getReader());
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        int codigo = jsonObject.getInt("codigo");
        JsonObject jsonResponse;
        try {
            mediDAO.destroy(codigo);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        jsonResponse = Json.createObjectBuilder()
                .add("success", true)
                .build();
        out.print(jsonResponse.toString());
        out.flush();

    }
}
