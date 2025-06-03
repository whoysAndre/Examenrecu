/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import dao.MedicoJpaController;
import dto.Medico;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author yello
 */
@WebServlet(name = "GenerarReporte", urlPatterns = {"/report"})
public class GenerarReporte extends HttpServlet {

    MedicoJpaController aluDAO = new MedicoJpaController();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InputStream reportStream = null;

        try {
            // 1. Traer los datos de la base de datos
            List<Medico> alumnos = aluDAO.findMedicoEntities();
            if (alumnos == null || alumnos.isEmpty()) {
                System.err.println("No se encontraron usuarios en la base de datos.");
                throw new IOException("No se encontraron usuarios en la base de datos.");
            }
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(alumnos);
            
            
            // 2. Cargar el .jrxml
            String reportPath = "/reportes/reportemedi.jrxml";
            reportStream = getServletContext().getResourceAsStream(reportPath);

            if (reportStream == null) {
                System.err.println("No se pudo encontrar el archivo en: " + reportPath);
                throw new IOException("No se pudo encontrar el archivo reportUsuarios.jrxml en " + reportPath);
            }
            
            // 3. Compilar el reporte
            JasperReport report = JasperCompileManager.compileReport(reportStream);

            // 4. Llenar el reporte con tu propio datasource
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, dataSource);
            
            // 5. Enviar como PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=reportUsuarios.pdf");

            OutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
