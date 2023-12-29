package org.marker.mushroom.filter;

import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 静态资源
 *
 * @author marker
 */
public class ThemesResourceFilter implements Filter {

    /**
     * 日志记录器
     */
    protected Logger logger = LoggerFactory.getLogger(ThemesResourceFilter.class);


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) req;
        ServletContext servletContext = request.getServletContext();

        String uri = WebUtils.getRequestUri(request);
        SystemConfig syscfg = SystemConfig.getInstance();

        setHeaders((HttpServletResponse) resp, getMediaType(servletContext, uri));
        String themesPath = syscfg.getThemesPath();
        String file = "";
        if (uri.startsWith("/themes")) {
            if (org.apache.commons.lang.StringUtils.isEmpty(themesPath)) {
                chain.doFilter(req, resp);
                return;
            } else {
                file = themesPath + uri.replaceAll("/themes", "");
            }
        } else if (uri.startsWith("/upload")) {
            if (org.apache.commons.lang.StringUtils.isEmpty(syscfg.getFilePath())) {
                chain.doFilter(req, resp);
                return;
            } else {
                file = syscfg.getFilePath() + uri;
            }
        }

        File fileInfo = new File(file);
        if (!fileInfo.exists() && !fileInfo.isFile()) {
            return;
        }

        long len = fileInfo.length();

        resp.setContentLength((int) len);

        InputStream inputStream = new FileInputStream(fileInfo);

        StreamUtils.copy(inputStream, resp.getOutputStream());
        inputStream.close();
        return;
    }


    @Override
    public void init(FilterConfig config) throws ServletException {
    }


    @Override
    public void destroy() {
    }


    protected void setHeaders(HttpServletResponse response, MediaType mediaType) throws IOException {

        if (mediaType != null) {
            response.setContentType(mediaType.toString());
        }
    }


    private static class ActivationMediaTypeFactory {
        private static final FileTypeMap fileTypeMap = loadFileTypeMapFromContextSupportModule();

        private ActivationMediaTypeFactory() {
        }

        private static FileTypeMap loadFileTypeMapFromContextSupportModule() {
            ClassPathResource mappingLocation = new ClassPathResource("org/springframework/mail/javamail/mime.types");
            if (mappingLocation.exists()) {
                InputStream inputStream = null;

                MimetypesFileTypeMap ex;
                try {
                    inputStream = mappingLocation.getInputStream();
                    ex = new MimetypesFileTypeMap(inputStream);
                } catch (IOException var12) {
                    return FileTypeMap.getDefaultFileTypeMap();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException var11) {
                            ;
                        }
                    }

                }

                return ex;
            } else {
                return FileTypeMap.getDefaultFileTypeMap();
            }
        }

        public static MediaType getMediaType(String filename) {
            String mediaType = fileTypeMap.getContentType(filename);
            return StringUtils.hasText(mediaType) ? MediaType.parseMediaType(mediaType) : null;
        }
    }


    protected MediaType getMediaType(ServletContext servletContext, String uri) {
        MediaType mediaType = null;
        String mimeType = servletContext.getMimeType(uri);
        if (StringUtils.hasText(mimeType)) {
            mediaType = MediaType.parseMediaType(mimeType);
        }
        return mediaType;
    }
}
