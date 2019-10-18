package automation.report.dao;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class AutoReportImpl implements AutoReportDao {
    private static AutoReportDao autoReportDao = null;
    private JdbcTemplate jdbcTemplate;

    public static AutoReportDao createInstance(String dataSource) {
        if (autoReportDao == null) {
            try {
                // 创建IOC容器
                ApplicationContext act = new ClassPathXmlApplicationContext("beans.xml");
                autoReportDao = (AutoReportDao) act.getBean(dataSource);

            } catch (BeansException e) {
                System.out.println(e.getMessage() + e);
            }
        }
        return autoReportDao;
    }

    // spring通过依赖注入自动给DAO装配dataSource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Object[][] getFurtherCheckData() {
        return null;
    }

    @Override
    public List<String> getCaseList() {
        List<String> caseList = new ArrayList<String>();
        List<?> rows = jdbcTemplate.queryForList("select case_name from test_case", new Object[]{});
        if (rows.size() > 0)
            for (int i = 0; i < rows.size(); i++) {
                caseList.add(rows.get(i).toString());
            }
        return caseList;
    }

    @Override
    public void update(String sql) {
        jdbcTemplate.update(sql);
    }

    @Override
    public void update(String sql, Object[] objs) {
        jdbcTemplate.update(sql, objs);
    }

    @Override
    public void update(String sql, Object[] objs, int[] types) {
        jdbcTemplate.update(sql, objs, types);
    }

    @Override
    public void update(String sql, String log) {
        jdbcTemplate.update(sql, new Object[]{log}, new int[]{java.sql.Types.VARCHAR});
    }

    @Override
    public boolean insertImage(String pic_id, byte[] in, String url, String createTime) {
        boolean flag = false;
        try {
            String insertPicture = "INSERT INTO screenshot_db (`picture_id`, `picture_long`, `url`, `create_time`) VALUES (?,?,?,?);";
            jdbcTemplate.update(insertPicture, new Object[]{pic_id, in, url, createTime}, new int[]{
                    java.sql.Types.VARCHAR, java.sql.Types.BLOB, java.sql.Types.VARCHAR, java.sql.Types.VARCHAR});
        } catch (DataAccessException e) {
            flag = true;
            e.printStackTrace();
        }

        return flag;
    }
}
