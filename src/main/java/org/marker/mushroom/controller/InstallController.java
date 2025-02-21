package org.marker.mushroom.controller;

import com.alibaba.druid.pool.DruidDataSource;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.DataSourceProxy;
import org.marker.mushroom.core.WebAPP;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.domain.MessageResult;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.FileTools;
import org.marker.mushroom.utils.GeneratePass;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.security.DES;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.*;


/**
 * 安装MRCMS引导
 *
 * @author marker
 */
@Controller
@RequestMapping("/install")
public class InstallController extends SupportController {


    public InstallController() {
        this.viewPath = "/install/";
    }



    @Resource
    private SystemConfig systemConfig;

    /**
     * 添加用户
     */
    @RequestMapping("/index")
    public ModelAndView add() {
        ModelAndView view = new ModelAndView(this.viewPath + "index");
        return view;
    }


    /**
     * 安装界面（配置数据库信息）
     */
    @RequestMapping("/install")
    public ModelAndView install() throws NoSuchAlgorithmException {
        ModelAndView view = new ModelAndView(this.viewPath + "install");

        String secretKey = DES.getSecretKey(null);
        view.addObject("secretKey", secretKey);

        return view;
    }


    /**
     * 数据库检查
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public MessageResult check(HttpServletRequest request) throws NoSuchAlgorithmException {
        Connection conn = null;
        try {
            String host = request.getParameter("DB_HOST");
            String port = request.getParameter("DB_PORT");
            String user = request.getParameter("DB_USER");
            String password = request.getParameter("DB_PWD");


            String url = "jdbc:mysql://" + host + ":" + port + "/" + "?useUnicode=true&characterEncoding=UTF-8";
            Class.forName("org.gjt.mm.mysql.Driver");

            conn = DriverManager.getConnection(url, user, password);
            return MessageResult.success();
        } catch (Exception e) {
            log.error("数据库连接失败！{}", e.getMessage());
            return MessageResult.error("数据库连接失败! <br/>" + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("数据库连接关闭失败！");
            }
        }
    }


    /**
     * 执行安装
     */
    @RequestMapping("/progress")
    public ModelAndView progress(HttpServletRequest
                                          request) throws NoSuchAlgorithmException {

        String exceptionStr = "";
        ModelAndView view = new ModelAndView(this.viewPath + "complete");
        // 项目真实路径
        String WebRootRealPath = application.getRealPath(File.separator);

        // 虚拟路径
        String WebRootPath = HttpUtils.getRequestURL(request);

        String host = request.getParameter("DB_HOST");
        String name = request.getParameter("DB_NAME");
        String port = request.getParameter("DB_PORT");
        String user = request.getParameter("DB_USER");
        String pass = request.getParameter("DB_PWD");
        String spot = request.getParameter("spot");//加密Key
        String prefix = request.getParameter("DB_PREFIX");//表前缀

        boolean status = (host != null) && name != null &&
                port != null && user != null && pass != null && spot != null && prefix != null;

        if (status) {
            String jdbcurl = "jdbc:mysql://" + host + ":" + port + "/" + "?useUnicode=true&characterEncoding=UTF-8";
            String jdbcDBurl = "jdbc:mysql://" + host + ":" + port + "/" + name + "?useUnicode=true&characterEncoding=UTF-8";


            try {

                /* ==============================================
                 *              1. 获取数据库链接
                 * ==============================================
                 */
                String driverClassName = "org.gjt.mm.mysql.Driver";
                Class.forName(driverClassName);

                Connection conn = DriverManager.getConnection(jdbcurl, user, pass);



                /* ==============================================
                 *              2. 数据库设置持久化
                 * ==============================================
                 */

                File file = new File( WebRootRealPath + "WEB-INF/classes/config.properties");

                DataBaseConfig dbc = DataBaseConfig.getInstance();
                dbc.read(file);
                dbc.set("mushroom.db.host", host);
                dbc.set("mushroom.db.port", port);
                dbc.set("mushroom.db.demo", name);
                dbc.set("mushroom.db.user", user);
                dbc.set("mushroom.db.pass", pass);
                dbc.set("mushroom.db.prefix", prefix);
                dbc.store();//保存



                /* ==============================================
                 *              3. 系统加密Key持久化
                 * ==============================================
                 */
                systemConfig.set("secret_key", spot);//更新Key
                systemConfig.store();//保存




                /* =======================================================
                 *   4. 获取数据库链接，并判断数据库是否存在，不存在就创建
                 * =======================================================
                 */
                String checkAndCreateSql = "CREATE database IF NOT EXISTS " + name;
                PreparedStatement ps = conn.prepareStatement(checkAndCreateSql);
                ps.executeUpdate();
                ps.close();



                /* =======================================================
                 *   5. 读取建表SQL信息，并创建表
                 * =======================================================
                 */
                File sqlFile = new File(WebRootRealPath + "/data/sql/db_app.sql");
                String sql = FileTools.getFileContet(sqlFile, FileTools.FILE_CHARACTER_UTF8);
                sql = sql.replaceAll("\\{prefix\\}", prefix);//替换前缀
//                System.out.println(sql);

                String[] sqla = sql.split(";");
                conn.setCatalog(name);
                Statement statement = conn.createStatement();
                System.out.println("===========");
                for (int i = 0; i < sqla.length; i++) {
                    String a = sqla[i];
                    if (a != null && !"".equals(a.trim())) {
                        log.info("{}", a);
//                        statement.addBatch(a);
                        statement.execute(a);
                    }
                }
                System.out.println("===========");
//                statement.executeBatch();
                statement.close();


                // 更新密码默认用户admin
                String pass2 = GeneratePass.encode("mrcms");
                ps = conn.prepareStatement("update " + prefix + "user set pass='" + pass2 + "' where id=1");
                ps.executeUpdate();
                ps.close();
                conn.close();

                //设置安装状态
                application.setAttribute(AppStatic.WEB_APP_INSTALL, true);
                String BasePath = application.getRealPath("/data/");
                OutputStream os = new FileOutputStream(new File(BasePath + "/install.lock"));
                os.write(0);
                os.flush();
                os.close();



                /* =======================================================
                 *   6. 数据库链接
                 * =======================================================
                 */

                DruidDataSource dataSource =new DruidDataSource();

                dataSource.setDriverClassName(driverClassName);
                dataSource.setUrl(jdbcDBurl);
                dataSource.setUsername(user);
                dataSource.setPassword(pass);


                DataSourceProxy proxy = SpringContextHolder.getBean("dataSource");
                proxy.setDataSource(dataSource);






                WebAPP.install = true;// 设置安装状态(必须)
            } catch (Exception e) {
                exceptionStr = e.getMessage();
                log.error("mrcms install exception", e);
            }
        }

        view.addObject("install", WebAPP.install);
        view.addObject("exceptionStr", exceptionStr);
        view.addObject("WebRootPath", WebRootPath);

        return view;
    }


}
