package idc.dao;

import idc.constants.Constants;
import idc.jdbc.JDBCHelper;
import idc.utils.TimeUtils;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by sky on 2017/3/11.
 */
public class TaskDAO implements JDBCHelper.QueryCallback {
//
//    public static void main(String[] args) {
//        ArrayList tasks = TaskDAO.getAll();
//        System.out.println(tasks);
//    }

    private ArrayList<TaskBean> tasks;

    public ArrayList<TaskBean> getAll() {
        String sql = "SELECT * FROM task";
        JDBCHelper.getInstanse().executeQuery(sql, null, this);
        return tasks;
    }

    @Override
    public void process(ResultSet rs) throws Exception {
        tasks = new ArrayList<>();
        while (rs.next()) {
            TaskBean task = new TaskBean();
            task.setTaskId(rs.getInt(Constants.TASK_ID));
            task.setLowerAge(rs.getInt(Constants.TASK_LOWER_AGE));
            task.setHigherAge(rs.getInt(Constants.TASK_HIGHER_AGE));
            task.setProfessional(rs.getString(Constants.TASK_PROFESSIONAL));
            task.setCity(rs.getString(Constants.TASK_CITY));
            task.setSearchWord(rs.getString(Constants.TASK_SEARCH_WORD));
            task.setCategory(rs.getString(Constants.TASK_CATEGORY));


            java.sql.Timestamp startSessionTime = rs.getTimestamp(Constants.TASK_START_SESSION_TIME);
            java.sql.Timestamp endSessionTime = rs.getTimestamp(Constants.TASK_END_SESSION_TIME);

            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
            if (startSessionTime != null && endSessionTime != null) {
                task.setStartSessionTime(TimeUtils.sqlToDate(startSessionTime));
                task.setEndSessionTime(TimeUtils.sqlToDate(endSessionTime));
            }
            tasks.add(task);
        }
    }
}
