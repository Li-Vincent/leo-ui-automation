package automation.report.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

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
    public boolean insertImage(String pic_id, byte[] in, String url, String startTime) {
        return false;
    }

    @Override
    public Object[][] getFurtherCheckData() {
        return null;
    }

    @Override
    public List<String> getCaseList() {
        List<String> caseList = new ArrayList<String>();
        List<?> rows = jdbcTemplate.queryForList("select case_name from test_case", new Object[] {});
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
        jdbcTemplate.update(sql, new Object[] { log }, new int[] { java.sql.Types.VARCHAR });
    }
}
