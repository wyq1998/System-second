package system2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**获得用户列表
 */
public class CheckUsers {
    GetConnection getConnection=new GetConnection();
    Connection connection=getConnection.GetConnection();
    public ArrayList<Users> getUsers(){
        ArrayList<Users>list = new ArrayList<>();
        try {

            PreparedStatement state=connection.prepareStatement("select *from users");
            ResultSet res=state.executeQuery();
            while(res.next()){
                Users user = new Users();
                user.setName(res.getString(1));
                user.setPassword(res.getString(2));
                user.setType(res.getInt(3));
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("读取失败");
        }
        return list;
    }
}

