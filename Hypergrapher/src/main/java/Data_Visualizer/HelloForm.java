package Data_Visualizer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HelloForm")
public class HelloForm extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public HelloForm() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Retrieve parameters from the request
        String fileinput = request.getParameter("fileinput");
        String graphtype = request.getParameter("graphtype");
        String data = request.getParameter("file-output");
        
        // Process the input data
        List<String[]> list = new ArrayList<>();
        String splitBy = ",";
        String[] lines = data.split("\\R"); // Split by new lines
        String[] labels = lines[0].split(splitBy); // First line for labels
        
        for (String line : lines) { 
            String[] values = line.split(splitBy);
            list.add(values);
        }

        // Prepare data for the chart
        StringBuilder datasets = new StringBuilder();
        StringBuilder xaxis = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                // Prepare x-axis labels
                for (int j = 1; j < list.get(i).length; j++) {
                    xaxis.append("'").append(list.get(i)[j]).append("'");
                    if (j < list.get(i).length - 1) xaxis.append(",");
                }
            } else {
                // Prepare datasets
                datasets.append("{")
                        .append("label: '").append(labels[i]).append("',")
                        .append("backgroundColor: 'rgba(").append(new Random().nextInt(256)).append(",").append(new Random().nextInt(256)).append(",").append(new Random().nextInt(256)).append(",0.5)',")
                        .append("borderColor: 'rgba(").append(new Random().nextInt(256)).append(",").append(new Random().nextInt(256)).append(",").append(new Random().nextInt(256)).append(",1)',")
                        .append("data: [");
                for (int j = 1; j < list.get(i).length; j++) {
                    datasets.append(list.get(i)[j]);
                    if (j < list.get(i).length - 1) datasets.append(",");
                }
                datasets.append("}");
                if (i < list.size() - 1) datasets.append(",");
            }
        }

        // Generate the HTML response
        out.print("<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "    <head>\r\n"
                + "        <title>Landing Page</title>\r\n"
                + "        <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\r\n"
                + "    </head>\r\n"
                + "    <body>\r\n"
                + "        <h1>Thanks for using <span>HyperGrapher</span>!</h1>\r\n"
                + "        <div>\r\n"
                + "            < div id=\"chartContainer\" style=\"width: 80%; height: 400px;\">\r\n"
                + "                <canvas id=\"myChart\"></canvas>\r\n"
                + "            </div>\r\n"
                + "        </div>\r\n"
                + "        <script>\r\n"
                + "            var ctx = document.getElementById('myChart').getContext('2d');\r\n"
                + "            var myChart = new Chart(ctx, {\r\n"
                + "                type: '" + graphtype + "',\r\n"
                + "                data: {\r\n"
                + "                    labels: [" + xaxis.toString() + "],\r\n"
                + "                    datasets: [" + datasets.toString() + "]\r\n"
                + "                },\r\n"
                + "                options: {\r\n"
                + "                    scales: {\r\n"
                + "                        y: {\r\n"
                + "                            beginAtZero: true\r\n"
                + "                        }\r\n"
                + "                    }\r\n"
                + "                }\r\n"
                + "            });\r\n"
                + "        </script>\r\n"
                + "    </body>\r\n"
                + "</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}