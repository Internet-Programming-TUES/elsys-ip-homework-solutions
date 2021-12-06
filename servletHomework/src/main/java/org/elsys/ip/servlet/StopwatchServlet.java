package org.elsys.ip.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class StopwatchServlet extends HttpServlet {

    private final Map<String, Stopwatch> map = new HashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (checkPath(req, resp, "start")) {
            UUID id = UUID.randomUUID();
            map.put(id.toString(), new Stopwatch());
            resp.getWriter().println(id);
            resp.setStatus(201);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (checkPath(req, resp, "<ID>")) {
            String id = getId(req);
            if (!map.containsKey(id)) {
                resp.setStatus(404);
                return;
            }

            Stopwatch st = map.get(id);
            resp.getWriter().print(formatDuration(st.get()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (checkPath(req, resp, "<ID>", "lap")) {
            String id = getId(req);
            if (!map.containsKey(id)) {
                resp.setStatus(404);
                return;
            }

            Stopwatch st = map.get(id);
            resp.getWriter().print(formatDuration(st.lap()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (checkPath(req, resp, "<ID>")) {
            String id = getId(req);
            if (!map.containsKey(id)) {
                resp.setStatus(404);
                return;
            }

            Stopwatch st = map.get(id);
            List<Stopwatch.Durations> durations = st.stop();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < durations.size(); ++i) {
                sb.append(String.format("%02d ", i+1));
                sb.append(formatDuration(durations.get(i).getFromLap()));
                sb.append(" / ");
                sb.append(formatDuration(durations.get(i).getFromBegin()));
                sb.append("\n");
            }
            map.remove(id);
            resp.getWriter().print(sb);
        }
    }

    private boolean checkPath(HttpServletRequest req, HttpServletResponse resp, String... args) {
        String pathss = req.getPathInfo() == null ? "" : req.getPathInfo();
        List<String> paths = Arrays.stream(pathss.split("/")).filter(x -> !x.equals("")).collect(Collectors.toList());

        if (paths.size() != args.length) {
            resp.setStatus(400);
            return false;
        }

        for (int i = 0; i < paths.size(); ++i) {
            String path = paths.get(i);
            String arg = args[i];

            if (!arg.equals("<ID>") && !path.equals(arg)) {
                resp.setStatus(400);
                return false;
            }
        }

        return true;
    }

    private String getId(HttpServletRequest req) {
        String pathss = req.getPathInfo() == null ? "" : req.getPathInfo();
        return Arrays.stream(pathss.split("/")).filter(x -> !x.equals("")).findFirst().get();
    }

    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%02d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
}