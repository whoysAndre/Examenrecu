/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import dao.MedicoJpaController;
import dto.Medico;
import java.io.IOException;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.BcryptJava;


@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    MedicoJpaController mediDAO = new MedicoJpaController();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        JsonReader jsonReader = Json.createReader(request.getReader());
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        String dni = jsonObject.getString("ndni");
        String password = jsonObject.getString("password");
        
        //Encontramos al usuario por el DNI
        Medico medico = mediDAO.findMedicoByDNI(dni);

        if (medico == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (!BcryptJava.checkPassword(password, medico.getPassMedi())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //Para mandar respuesta
        JsonObject jsonResponse;

        jsonResponse = Json.createObjectBuilder()
                .add("success", true)
                .add("username", medico.getLogiMedi())
                .build();
        out.print(jsonResponse.toString());
        out.flush();

    }

    
}
