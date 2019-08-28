package automation.report.dao;

import java.util.List;

public interface AutoReportDao {

    public void update(String sql);

    public void update(String sql, Object[] objs);

    public void update(String sql, Object[] objs, int[] types);

    public void update(String sql, String log);

    public boolean insertImage(String pic_id, byte[] in, String url, String startTime);

    public Object[][] getFurtherCheckData();

    public List<String> getCaseList();
}
