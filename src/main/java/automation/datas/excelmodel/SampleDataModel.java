package automation.datas.excelmodel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SampleDataModel extends BaseDataModel {
    @ExcelProperty(value = "姓名", index = 2)
    private String name;

    @ExcelProperty(value = "年龄", index = 3)
    private String age;

    @ExcelProperty(value = "邮箱", index = 4)
    private String email;

    @ExcelProperty(value = "地址", index = 5)
    private String address;

    @ExcelProperty(value = "性别", index = 6)
    private String gender;

    @ExcelProperty(value = "高度", index = 7)
    private String height;

    @ExcelProperty(value = "备注", index = 8)
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static Map<String, SampleDataModel> read(String filePath) {
        final Map<String, SampleDataModel> datas = new ConcurrentHashMap<String, SampleDataModel>();
        try (InputStream in = new FileInputStream(filePath);) {
            AnalysisEventListener<SampleDataModel> listener = new AnalysisEventListener<SampleDataModel>() {

                @Override
                public void invoke(SampleDataModel data, AnalysisContext context) {
                    datas.put(data.getTestName() + data.getScenario(), data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            };

            EasyExcel.read(in, SampleDataModel.class, listener).sheet(0).doRead();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datas;
    }
}
