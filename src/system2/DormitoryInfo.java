package system2;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 宿舍信息(学生)
 */
public class DormitoryInfo extends JPanel implements ActionListener {
	Connection connection = new GetConnection().GetConnection();
	Users users;// 当前用户
	int type;// 用户类型
	String Phone = "";// 宿舍号
	JTable table = new JTable();
	String[] col = { "学号", "姓名", "有无感染", "学院", "联系电话", "登记时间" };
	DefaultTableModel mm = new DefaultTableModel(col, 0); // 定义一个表的模板
	JLabel sno, name, Sdept, suse, es, time;
	JTextField snoText, nameText, SdeptText, suseText, esText, timeText;
	JButton add;
	JButton delete;
	JButton submit;
	JButton dao, dao1, dao2, dao3, dao4, dao5;
	JButton show, show1;
	JButton jchart;

	JPanel suguan;

	static ChartPanel frame1;
	static ChartPanel frame2;
	static int a1, a2;
	static int x;// 判断生成那项数据的统计图
	static JFreeChart chart;
	static String[] data0;

	private DormitoryInfo() {

		if (x == 1) {
			CategoryDataset dataset = getDataSet();// 将获得的数据传递给CategoryDataset类的对象
			chart = ChartFactory.createBarChart3D("感染人数统计", // 图表标题
					"有无感染", // 目录轴的显示标签
					"人数/个", // 数值轴的显示标签
					dataset, // 数据集
					PlotOrientation.VERTICAL, // 图表方向：水平、垂直
					true, // 是否显示图例(对于简单的柱状图必须是false)
					false, // 是否生成工具
					false // 是否生成URL链接
			);
		}
		if (x == 2) {
			CategoryDataset dataset = getDataSet1();// 将获得的数据传递给CategoryDataset类的对象
			chart = ChartFactory.createBarChart3D("填写人数统计", // 图表标题
					"有无填写", // 目录轴的显示标签
					"人数/个", // 数值轴的显示标签
					dataset, // 数据集
					PlotOrientation.VERTICAL, // 图表方向：水平、垂直
					true, // 是否显示图例(对于简单的柱状图必须是false)
					false, // 是否生成工具
					false // 是否生成URL链接
			);
		}
		CategoryPlot plot = chart.getCategoryPlot();// 获取图表区域对象
		CategoryAxis domainAxis = plot.getDomainAxis(); // 水平底部列表
		domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14)); // 水平底部标题
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12)); // 垂直标题
		ValueAxis rangeAxis = plot.getRangeAxis();// 获取柱状
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		chart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));// 设置标题字体

		frame1 = new ChartPanel(chart, true); // 这里也可以用chartFrame,可以直接生成一个独立的Frame

		DefaultPieDataset data = getDataSet2();
		JFreeChart chart = ChartFactory.createPieChart3D("感染与未感染比例", data, true, false, false);
		// 设置百分比
		PiePlot pieplot = (PiePlot) chart.getPlot();
		DecimalFormat df = new DecimalFormat("0.00%");// 获得一个DecimalFormat对象，主要是设置小数问题
		NumberFormat nf = NumberFormat.getNumberInstance();// 获得一个NumberFormat对象
		StandardPieSectionLabelGenerator sp1 = new StandardPieSectionLabelGenerator("{0}  {2}", nf, df);// 获得StandardPieSectionLabelGenerator对象
		pieplot.setLabelGenerator(sp1);// 设置饼图显示百分比

		// 没有数据的时候显示的内容
		pieplot.setNoDataMessage("无数据显示");
		pieplot.setCircular(false);
		pieplot.setLabelGap(0.02D);

		pieplot.setIgnoreNullValues(true);// 设置不显示空值
		pieplot.setIgnoreZeroValues(true);// 设置不显示负值
		frame2 = new ChartPanel(chart, true);
		chart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));// 设置标题字体
		PiePlot piePlot = (PiePlot) chart.getPlot();// 获取图表区域对象
		piePlot.setLabelFont(new Font("宋体", Font.BOLD, 10));// 解决乱码
		chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 10));
	}

	private static CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(a1, "感染", "感染");// 成绩1
		dataset.addValue(a2, "未感染", "未感染");// 成绩2
		return dataset;
	}

	private static CategoryDataset getDataSet1() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(a1, "填写", "填写");// 成绩1
		dataset.addValue(a2 - a1, "未填写", "未填写");// 成绩2
		return dataset;
	}

	private static DefaultPieDataset getDataSet2() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("感染", a1);
		dataset.setValue("未感染", a2);
		return dataset;
	}

	public ChartPanel getChartPanel() {
		return frame1;

	}

	public ChartPanel getChartPanel1() {
		return frame2;

	}

	public DormitoryInfo(Users users, int type) {// 从登录界面传回，用户名和用户类型
		this.type = type;
		this.users = users;
		setLayout(new FlowLayout());

		table.setModel(mm);
		table.setRowSorter(new TableRowSorter<>(mm));// 排序
		JPanel jPanel = new JPanel(new FlowLayout());
		JScrollPane js = new JScrollPane(table);
		jPanel.add(js);

		add(jPanel);
		search();
	}

	private void search() {
		PreparedStatement state;
		ResultSet resultSet;
		if (type == 1) {// 学生
			try {
				inquire();
				String select = "select Phone from ES where Sname" + "=" + "'" + users.getName() + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					Phone = resultSet.getString("Phone");
				}
				System.out.println(users.getName() + users.getName().length());
				select = "select*from ES where Phone" + "=" + "'" + Phone + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String Dno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, Dno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (type == 2) {// 二级防疫部门
			try {
				inquire1();
				String select = "select Phone from ES where Sname" + "=" + "'" + users.getName() + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					Phone = resultSet.getString("Phone");
				}
				System.out.println(users.getName() + users.getName().length());
				select = "select*from ES where Phone" + "=" + "'" + Phone + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String Dno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, Dno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (type == 3) {// 二级防疫部门负责人
			try {
				xiugai();
				int n = 2;
				state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String Dno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, Dno, Scheckin };
					mm.addRow(data);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (type == 4) {// 防控办
			try {
				xiugai1();
				state = connection.prepareStatement("select *from ES");
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String Dno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, Dno, Scheckin };
					mm.addRow(data);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (type == 5) {// 教职工
			try {
				inquire2();
				String select = "select Phone from ES where Sname" + "=" + "'" + users.getName() + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					Phone = resultSet.getString("Phone");
				}
				System.out.println(users.getName() + users.getName().length());
				select = "select*from ES where Phone" + "=" + "'" + Phone + "'";
				state = connection.prepareStatement(select);
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String Dno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, Dno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void inquire() {// 学生
		sno = new JLabel("学号");
		Sdept = new JLabel("学院");
		suse = new JLabel("电话");
		es = new JLabel("感染情况");
		time = new JLabel("登记时间");
		snoText = new JTextField(10);
		SdeptText = new JTextField(10);
		suseText = new JTextField(10);
		esText = new JTextField(10);
		timeText = new JTextField(10);
		name = new JLabel("名字");
		nameText = new JTextField(10);
		suguan = new JPanel(new GridLayout(7, 2));
		add = new JButton("提交");
		add.addActionListener(this);
		sno.setFont(new Font("宋体", Font.PLAIN, 18));
		name.setFont(new Font("宋体", Font.PLAIN, 18));
		Sdept.setFont(new Font("宋体", Font.PLAIN, 18));
		suse.setFont(new Font("宋体", Font.PLAIN, 18));
		es.setFont(new Font("宋体", Font.PLAIN, 18));
		time.setFont(new Font("宋体", Font.PLAIN, 18));
		snoText.setBounds(0, 0, 240, 40);
		nameText.setBounds(0, 0, 240, 40);
		SdeptText.setBounds(0, 0, 240, 40);
		suseText.setBounds(0, 0, 240, 40);
		esText.setBounds(0, 0, 240, 40);
		timeText.setBounds(0, 0, 240, 40);
		suguan.add(sno);
		suguan.add(snoText);
		suguan.add(name);
		suguan.add(nameText);
		suguan.add(es);
		suguan.add(esText);
		suguan.add(Sdept);
		suguan.add(SdeptText);
		suguan.add(suse);
		suguan.add(suseText);
		suguan.add(time);
		suguan.add(timeText);
		add(suguan);
		suguan.add(add);
	}

	private void inquire1() {// 二级防疫部门
		sno = new JLabel("学号");
		Sdept = new JLabel("所属部门");
		suse = new JLabel("电话");
		es = new JLabel("感染情况");
		time = new JLabel("登记时间");
		snoText = new JTextField(10);
		SdeptText = new JTextField(10);
		suseText = new JTextField(10);
		esText = new JTextField(10);
		timeText = new JTextField(10);
		name = new JLabel("名字");
		nameText = new JTextField(10);
		suguan = new JPanel(new GridLayout(7, 2));
		add = new JButton("提交");
		add.addActionListener(this);
		sno.setFont(new Font("宋体", Font.PLAIN, 18));
		name.setFont(new Font("宋体", Font.PLAIN, 18));
		Sdept.setFont(new Font("宋体", Font.PLAIN, 18));
		suse.setFont(new Font("宋体", Font.PLAIN, 18));
		es.setFont(new Font("宋体", Font.PLAIN, 18));
		time.setFont(new Font("宋体", Font.PLAIN, 18));
		snoText.setBounds(0, 0, 240, 40);
		nameText.setBounds(0, 0, 240, 40);
		SdeptText.setBounds(0, 0, 240, 40);
		suseText.setBounds(0, 0, 240, 40);
		esText.setBounds(0, 0, 240, 40);
		timeText.setBounds(0, 0, 240, 40);
		suguan.add(sno);
		suguan.add(snoText);
		suguan.add(name);
		suguan.add(nameText);
		suguan.add(es);
		suguan.add(esText);
		suguan.add(Sdept);
		suguan.add(SdeptText);
		suguan.add(suse);
		suguan.add(suseText);
		suguan.add(time);
		suguan.add(timeText);
		add(suguan);
		suguan.add(add);
	}

	private void inquire2() {// 教职工
		sno = new JLabel("学号");
		Sdept = new JLabel("所属部门");
		suse = new JLabel("电话");
		es = new JLabel("感染情况");
		time = new JLabel("登记时间");
		snoText = new JTextField(10);
		SdeptText = new JTextField(10);
		suseText = new JTextField(10);
		esText = new JTextField(10);
		timeText = new JTextField(10);
		name = new JLabel("名字");
		nameText = new JTextField(10);
		suguan = new JPanel(new GridLayout(7, 2));
		add = new JButton("提交");
		add.addActionListener(this);
		sno.setFont(new Font("宋体", Font.PLAIN, 18));
		name.setFont(new Font("宋体", Font.PLAIN, 18));
		Sdept.setFont(new Font("宋体", Font.PLAIN, 18));
		suse.setFont(new Font("宋体", Font.PLAIN, 18));
		es.setFont(new Font("宋体", Font.PLAIN, 18));
		time.setFont(new Font("宋体", Font.PLAIN, 18));
		snoText.setBounds(0, 0, 240, 40);
		nameText.setBounds(0, 0, 240, 40);
		SdeptText.setBounds(0, 0, 240, 40);
		suseText.setBounds(0, 0, 240, 40);
		esText.setBounds(0, 0, 240, 40);
		timeText.setBounds(0, 0, 240, 40);
		suguan.add(sno);
		suguan.add(snoText);
		suguan.add(name);
		suguan.add(nameText);
		suguan.add(es);
		suguan.add(esText);
		suguan.add(Sdept);
		suguan.add(SdeptText);
		suguan.add(suse);
		suguan.add(suseText);
		suguan.add(time);
		suguan.add(timeText);
		add(suguan);
		suguan.add(add);
	}

	private void xiugai() {// 二级防疫部门负责人
		sno = new JLabel("学号");
		Sdept = new JLabel("所属部门");
		suse = new JLabel("电话");
		es = new JLabel("感染情况");
		time = new JLabel("登记时间");
		snoText = new JTextField(10);
		SdeptText = new JTextField(10);
		suseText = new JTextField(10);
		esText = new JTextField(10);
		timeText = new JTextField(10);
		name = new JLabel("名字");
		nameText = new JTextField(10);
		suguan = new JPanel(new GridLayout(10, 2));
		add = new JButton("增加");
		add.addActionListener(this);
		delete = new JButton("删除(更新)");
		delete.addActionListener(this);
		submit = new JButton("修改");
		submit.addActionListener(this);
		dao = new JButton("学号准确查询");
		dao.addActionListener(this);
		dao1 = new JButton("姓名模糊查询");
		dao1.addActionListener(this);
		dao2 = new JButton("时间准确查询");
		dao2.addActionListener(this);
		dao3 = new JButton("感染情况查询");
		dao3.addActionListener(this);
		jchart = new JButton("感染情况统计图");
		jchart.addActionListener(this);
		sno.setFont(new Font("宋体", Font.PLAIN, 18));
		name.setFont(new Font("宋体", Font.PLAIN, 18));
		Sdept.setFont(new Font("宋体", Font.PLAIN, 18));
		suse.setFont(new Font("宋体", Font.PLAIN, 18));
		es.setFont(new Font("宋体", Font.PLAIN, 18));
		time.setFont(new Font("宋体", Font.PLAIN, 18));
		snoText.setBounds(0, 0, 240, 40);
		nameText.setBounds(0, 0, 240, 40);
		SdeptText.setBounds(0, 0, 240, 40);
		suseText.setBounds(0, 0, 240, 40);
		esText.setBounds(0, 0, 240, 40);
		timeText.setBounds(0, 0, 240, 40);
		suguan.add(sno);
		suguan.add(snoText);
		suguan.add(name);
		suguan.add(nameText);
		suguan.add(es);
		suguan.add(esText);
		suguan.add(Sdept);
		suguan.add(SdeptText);
		suguan.add(suse);
		suguan.add(suseText);
		suguan.add(time);
		suguan.add(timeText);
		add(suguan);
		suguan.add(add);
		suguan.add(delete);
		suguan.add(submit);
		suguan.add(dao);
		suguan.add(dao1);
		suguan.add(dao2);
		suguan.add(dao3);
		suguan.add(jchart);
	}

	private void xiugai1() {// 防控办
		sno = new JLabel("学号");
		Sdept = new JLabel("所属单位");
		suse = new JLabel("电话");
		es = new JLabel("感染情况");
		time = new JLabel("登记时间");
		snoText = new JTextField(10);
		SdeptText = new JTextField(10);
		suseText = new JTextField(10);
		esText = new JTextField(10);
		timeText = new JTextField(10);
		name = new JLabel("名字");
		nameText = new JTextField(10);
		suguan = new JPanel(new GridLayout(12, 2));
		add = new JButton("增加");
		add.addActionListener(this);
		delete = new JButton("删除(更新)");
		delete.addActionListener(this);
		submit = new JButton("修改");
		submit.addActionListener(this);
		dao = new JButton("学号准确查询");
		dao.addActionListener(this);
		dao1 = new JButton("姓名模糊查询");
		dao1.addActionListener(this);
		dao2 = new JButton("时间准确查询");
		dao2.addActionListener(this);
		dao3 = new JButton("感染情况查询");
		dao3.addActionListener(this);
		jchart = new JButton("感染情况统计图");
		jchart.addActionListener(this);
		dao4 = new JButton("查询某人某天情况");
		dao4.addActionListener(this);
		dao5 = new JButton("填报情况统计图");
		dao5.addActionListener(this);
		show = new JButton("学生统计信息汇总");
		show.addActionListener(this);
		show1 = new JButton("教职工统计信息汇总");
		show1.addActionListener(this);
		sno.setFont(new Font("宋体", Font.PLAIN, 18));
		name.setFont(new Font("宋体", Font.PLAIN, 18));
		Sdept.setFont(new Font("宋体", Font.PLAIN, 18));
		suse.setFont(new Font("宋体", Font.PLAIN, 18));
		es.setFont(new Font("宋体", Font.PLAIN, 18));
		time.setFont(new Font("宋体", Font.PLAIN, 18));
		snoText.setBounds(0, 0, 240, 40);
		nameText.setBounds(0, 0, 240, 40);
		SdeptText.setBounds(0, 0, 240, 40);
		suseText.setBounds(0, 0, 240, 40);
		esText.setBounds(0, 0, 240, 40);
		timeText.setBounds(0, 0, 240, 40);
		suguan.add(sno);
		suguan.add(snoText);
		suguan.add(name);
		suguan.add(nameText);
		suguan.add(es);
		suguan.add(esText);
		suguan.add(Sdept);
		suguan.add(SdeptText);
		suguan.add(suse);
		suguan.add(suseText);
		suguan.add(time);
		suguan.add(timeText);
		add(suguan);
		suguan.add(add);
		suguan.add(delete);
		suguan.add(submit);
		suguan.add(dao);
		suguan.add(dao1);
		suguan.add(dao2);
		suguan.add(dao3);
		suguan.add(jchart);
		suguan.add(dao4);
		suguan.add(dao5);
		suguan.add(show);
		suguan.add(show1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// *** 二级防控办负责人部分 ***
		if (e.getSource() == add && type == 3) {// 增加
			JOptionPane.showMessageDialog(null, "插入已成功！", "警告", JOptionPane.WARNING_MESSAGE);
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("insert into dbo.ES values(?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println("插入失败！\n");
				e1.printStackTrace();
			}
			try {
				statement.setString(1, snoText.getText());
			} catch (SQLException e2) {
				System.out.println("失败！\n");
				e2.printStackTrace();
			}
			try {
				statement.setString(2, nameText.getText());
			} catch (SQLException e3) {
				System.out.println("失败！\n");
				e3.printStackTrace();
			}
			try {
				statement.setString(3, esText.getText());
			} catch (SQLException e4) {
				System.out.println("失败！\n");
				e4.printStackTrace();
			}
			try {
				statement.setString(4, SdeptText.getText());
			} catch (SQLException e5) {
				System.out.println("失败！\n");
				e5.printStackTrace();
			}
			try {
				statement.setString(5, suseText.getText());
			} catch (SQLException e6) {
				System.out.println("失败！\n");
				e6.printStackTrace();
			}
			try {
				statement.setString(6, timeText.getText());
			} catch (SQLException e7) {
				System.out.println("失败！\n");
				e7.printStackTrace();
			}
			try {
				int c = 2;
				statement.setLong(7, c);
			} catch (SQLException e7) {
				System.out.println("失败！\n");
				e7.printStackTrace();
			}

			try {
				statement.executeUpdate();
			} catch (SQLException e1) {
				System.out.println("失败！\n");
				e1.printStackTrace();
			}
			try {
				PreparedStatement state;
				ResultSet resultSet;
				int n = 2;
				state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}
		if (e.getSource() == delete && type == 3) {// 删除
			JOptionPane.showMessageDialog(null, "删除已成功！", "警告", JOptionPane.WARNING_MESSAGE);
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "delete from ES where Sname =" + "'" + nameText.getText() + "'" + "and Sno=" + "'"
						+ snoText.getText() + "'" + "and Phone=" + "'" + suseText.getText() + "'" + "and Province="
						+ "'" + SdeptText.getText() + "'" + "and Es=" + "'" + esText.getText() + "'" + "and Scheckin="
						+ "'" + timeText.getText() + "'";
				statement.executeUpdate(sql);
				PreparedStatement state;
				ResultSet resultSet;
				int n = 2;
				state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == submit && type == 3) {// 修改
			try {
				if (esText.getText().length() > 0 && SdeptText.getText().length() == 0
						&& timeText.getText().length() > 0) {// 只修改感染情况

					Statement statement = connection.createStatement();
					String sql = "update ES set Es=" + "'" + esText.getText() + "'" + "where Sno" + "=" + "'"
							+ snoText.getText() + "'" + "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					int n = 2;
					state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {// 把更新后的数据重新显示到表格中，下同
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
				if (esText.getText().length() == 0 && SdeptText.getText().length() > 0
						&& timeText.getText().length() > 0) {// 只修改所在省份
					Statement statement = connection.createStatement();
					String sql = "update ES set Province=" + "'" + SdeptText.getText() + "'" + "where Sno" + "=" + "'"
							+ snoText.getText() + "'" + "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					int n = 2;
					state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
				if (esText.getText().length() > 0 && SdeptText.getText().length() > 0
						&& timeText.getText().length() > 0) {// 同时修改
					Statement statement = connection.createStatement();
					String sql = "update ES set Province=" + "'" + SdeptText.getText() + "'" + ", Es=" + "'"
							+ esText.getText() + "'" + "where Sno" + "=" + "'" + snoText.getText() + "'"
							+ "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					int n = 2;
					state = connection.prepareStatement("select *from ES where Utype" + "=" + "'" + n + "'");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao && type == 3) {// 根据学号进行准确查询
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Sno =" + "'" + snoText.getText() + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao1 && type == 3) {// 姓名进行模糊查询
			Statement statement;
			try {
				statement = connection.createStatement();

				int n = 2;
				String sql = "select *from ES where Sname like'%" + nameText.getText() + "%'" + "and Utype" + "=" + "'"
						+ n + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao2 && type == 3) {// 时间准确查询
			Statement statement;
			try {
				statement = connection.createStatement();

				int n = 2;
				String sql = "select *from ES where Scheckin =" + "'" + timeText.getText() + "'" + "and Utype" + "="
						+ "'" + n + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao3 && type == 3) {// 感染查询
			Statement statement;
			try {
				statement = connection.createStatement();

				int n = 2;
				String sql = "select *from ES where Es =" + "'" + esText.getText() + "'" + "and Utype" + "=" + "'" + n
						+ "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);

					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == jchart && type == 3) {// 生成统计图
			a1 = 0;
			a2 = 0;
			x = 1;
			int n = 2;
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection.prepareStatement("select *from ES where Scheckin =" + "'" + timeText.getText() + "'"
						+ "and Utype =" + "'" + n + "'");
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);
					if (Ssex.equals("有")) {
						a1++;
					} else if (Ssex.equals("无")) {
						a2++;
					}
				}
				JFrame frame = new JFrame("感染人数统计图");
				frame.setLayout(new GridLayout(1, 1, 5, 5));
				frame.add(new DormitoryInfo().getChartPanel()); // 添加柱形图
				frame.setBounds(0, 0, 500, 600);
				frame.setVisible(true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == add && type == 4) {// 增加
			String a = JOptionPane.showInputDialog("请输入您要插入的人员类型（1学生2学校二级防疫部门成员3学校二级防疫部门负责人4学校防控办）:");
			JOptionPane.showMessageDialog(null, "插入已成功！", "警告", JOptionPane.WARNING_MESSAGE);
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("insert into dbo.ES values(?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println("插入失败！\n");
				e1.printStackTrace();
			}
			try {
				statement.setString(1, snoText.getText());
			} catch (SQLException e2) {
				System.out.println("失败！\n");
				e2.printStackTrace();
			}
			try {
				statement.setString(2, nameText.getText());
			} catch (SQLException e3) {
				System.out.println("失败！\n");
				e3.printStackTrace();
			}
			try {
				statement.setString(3, esText.getText());
			} catch (SQLException e4) {
				System.out.println("失败！\n");
				e4.printStackTrace();
			}
			try {
				statement.setString(4, SdeptText.getText());
			} catch (SQLException e5) {
				System.out.println("失败！\n");
				e5.printStackTrace();
			}
			try {
				statement.setString(5, suseText.getText());
			} catch (SQLException e6) {
				System.out.println("失败！\n");
				e6.printStackTrace();
			}
			try {
				statement.setString(6, timeText.getText());
			} catch (SQLException e7) {
				System.out.println("失败！\n");
				e7.printStackTrace();
			}
			try {
				statement.setString(7, a);
			} catch (SQLException e7) {
				System.out.println("失败！\n");
				e7.printStackTrace();
			}

			try {
				statement.executeUpdate();
			} catch (SQLException e1) {
				System.out.println("失败！\n");
				e1.printStackTrace();
			}
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection.prepareStatement("select *from ES");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}
		if (e.getSource() == delete && type == 4) {// 删除
			JOptionPane.showMessageDialog(null, "删除已成功！", "警告", JOptionPane.WARNING_MESSAGE);
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "delete from ES where Sname =" + "'" + nameText.getText() + "'" + "and Sno=" + "'"
						+ snoText.getText() + "'" + "and Phone=" + "'" + suseText.getText() + "'" + "and Province="
						+ "'" + SdeptText.getText() + "'" + "and Es=" + "'" + esText.getText() + "'" + "and Scheckin="
						+ "'" + timeText.getText() + "'";
				statement.executeUpdate(sql);
				PreparedStatement state;
				ResultSet resultSet;
				state = connection.prepareStatement("select *from ES");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == submit && type == 4) {// 修改
			try {
				if (esText.getText().length() > 0 && SdeptText.getText().length() == 0
						&& timeText.getText().length() > 0) {// 只修改感染情况

					Statement statement = connection.createStatement();
					String sql = "update ES set Es=" + "'" + esText.getText() + "'" + "where Sno" + "=" + "'"
							+ snoText.getText() + "'" + "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					state = connection.prepareStatement("select *from ES");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {// 把更新后的数据重新显示到表格中，下同
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
				if (esText.getText().length() == 0 && SdeptText.getText().length() > 0
						&& timeText.getText().length() > 0) {// 只修改所在省份
					Statement statement = connection.createStatement();
					String sql = "update ES set Province=" + "'" + SdeptText.getText() + "'" + "where Sno" + "=" + "'"
							+ snoText.getText() + "'" + "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					state = connection.prepareStatement("select *from ES");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
				if (esText.getText().length() > 0 && SdeptText.getText().length() > 0
						&& timeText.getText().length() > 0) {// 同时修改
					Statement statement = connection.createStatement();
					String sql = "update ES set Province=" + "'" + SdeptText.getText() + "'" + ", Es=" + "'"
							+ esText.getText() + "'" + "where Sno" + "=" + "'" + snoText.getText() + "'"
							+ "and Scheckin=" + "'" + timeText.getText() + "'";
					statement.executeUpdate(sql);
					PreparedStatement state;
					ResultSet resultSet;
					state = connection.prepareStatement("select *from ES");
					resultSet = state.executeQuery();
					while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
						// System.out.println(model.getRowCount());
						mm.removeRow(mm.getRowCount() - 1);
					}
					while (resultSet.next()) {
						String Sno = resultSet.getString(1);
						String Sname = resultSet.getString(2);
						String Ssex = resultSet.getString(3);
						String Sdept = resultSet.getString(4);
						String DDno = resultSet.getString(5);
						String Scheckin = resultSet.getString(6);
						String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
						mm.addRow(data);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao && type == 4) {// 根据学号进行准确查询
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Sno =" + "'" + snoText.getText() + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao1 && type == 4) {// 姓名进行模糊查询
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Sname like'%" + nameText.getText() + "%'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao2 && type == 4) {// 时间准确查询
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Scheckin =" + "'" + timeText.getText() + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao3 && type == 4) {// 感染查询
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Es =" + "'" + esText.getText() + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);

					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao4 && type == 4) {// 某人某天情况查询
			Statement statement;
			try {
				statement = connection.createStatement();

				String sql = "select *from ES where Scheckin =" + "'" + timeText.getText() + "'" + "and Sno =" + "'"
						+ snoText.getText() + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);

					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == jchart && type == 4) {// 生成统计图
			a1 = 0;
			a2 = 0;
			x = 1;
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection
						.prepareStatement("select *from ES where Scheckin =" + "'" + timeText.getText() + "'");
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);
					if (Ssex.equals("有")) {
						a1++;
					} else if (Ssex.equals("无")) {
						a2++;
					}
				}
				JFrame frame = new JFrame("感染人数统计图");
				frame.setLayout(new GridLayout(1, 1, 5, 5));
				frame.add(new DormitoryInfo().getChartPanel()); // 添加柱形图
				frame.setBounds(0, 0, 500, 600);
				frame.setVisible(true);
				String a = JOptionPane.showInputDialog("是否生成相应的饼状图:");
				if (a.equals("是")) {
					JFrame frame1 = new JFrame("饼状图");
					frame1.add(new DormitoryInfo().getChartPanel1()); // 添加柱形图
					frame1.setBounds(0, 0, 500, 600);
					frame1.setVisible(true);
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == dao5 && type == 4) {// 生成统计图1
			a1 = 0;
			String a = JOptionPane.showInputDialog("请输入该学院的总人数:");
			a2 = Integer.parseInt(a);

			x = 2;

			ArrayList<String> list = new ArrayList<String>();
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection.prepareStatement("select *from ES where Scheckin =" + "'" + timeText.getText() + "'"
						+ "and Province =" + "'" + SdeptText.getText() + "'");
				resultSet = state.executeQuery();
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					list.add(resultSet.getString(1));
					list.add(resultSet.getString(2));
					list.add(resultSet.getString(3));
					list.add(resultSet.getString(4));
					list.add(resultSet.getString(5));
					list.add(resultSet.getString(6));
					System.out.println(Sno + "," + Sname + "," + Ssex + "," + Sdept + "," + DDno + "," + Scheckin);
					a1++;
				}

				if (list != null && list.size() > 0) {// 如果list中存入了数据，转化为数组
					data0 = new String[list.size()];// 创建一个和list长度一样的数组
					for (int i = 0; i < list.size(); i++) {
						data0[i] = list.get(i);// 数组
					}
				}

				JFrame frame = new JFrame("填写人数统计图");
				frame.setLayout(new GridLayout(1, 1, 5, 5));
				frame.add(new DormitoryInfo().getChartPanel()); // 添加柱形图
				frame.setBounds(0, 0, 500, 600);
				frame.setVisible(true);
				String b = JOptionPane.showInputDialog("请输入是否导出excel:");
				if (b.equals("是")) {
					try {
						// 打开文件
WritableWorkbook book = Workbook.createWorkbook(new File("C:\\\\Users\\\\ASUS\\\\Desktop\\\\x.xls"));
						// 生成名为“第一页”的工作表，参数0表示这是第一页
						WritableSheet sheet = book.createSheet("第一页", 0);
						Label label1 = new Label(0, 0, "学号");// 对应为第1列第1行的数据
						Label label2 = new Label(1, 0, "姓名");// 对应为第2列第1行的数据
						Label label3 = new Label(2, 0, "有无感染");// 对应为第3列第1行的数据
						Label label4 = new Label(3, 0, "学院");// 对应为第4列第1行的数据
						Label label5 = new Label(4, 0, "联系电话");// 对应为第5列第1行的数据
						Label label6 = new Label(5, 0, "登记时间");// 对应为第6列第1行的数据
						// 添加单元格到选项卡中
						sheet.addCell(label1);
						sheet.addCell(label2);
						sheet.addCell(label3);
						sheet.addCell(label4);
						sheet.addCell(label5);
						sheet.addCell(label6);
						System.out.println((data0.length) / 6);
						for (int i = 0; i < data0.length; i++) {
							Label label = new Label(i, 0, data0[i]);
							sheet.addCell(label);
						}
						// 写入数据并关闭文件
						book.write();
						book.close();
					} catch (Exception e1) {
						System.out.println(e1);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == show && type == 4) {// 学生统计信息汇总
			Statement statement;
			try {
				statement = connection.createStatement();
				int z = 1;
				String sql = "select *from ES where Utype =" + "'" + z + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };

					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == show1 && type == 4) {// 教职工统计信息汇总
			Statement statement;
			try {
				statement = connection.createStatement();
				int z = 5;
				String sql = "select *from ES where Utype =" + "'" + z + "'";
				ResultSet resultSet;
				resultSet = statement.executeQuery(sql);
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };

					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		// ***学生***
		if (e.getSource() == add && type == 1) {// 学生
			JOptionPane.showMessageDialog(null, "插入已成功！", "警告", JOptionPane.WARNING_MESSAGE);
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("insert into dbo.ES values(?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println("插入失败！\n");
				e1.printStackTrace();
			}
			try {
				statement.setString(1, snoText.getText());
			} catch (SQLException e2) {
				System.out.println("失败！\n");
				e2.printStackTrace();
			}
			try {
				statement.setString(2, nameText.getText());
			} catch (SQLException e3) {
				System.out.println("失败！\n");
				e3.printStackTrace();
			}
			try {
				statement.setString(3, esText.getText());
			} catch (SQLException e4) {
				System.out.println("失败！\n");
				e4.printStackTrace();
			}
			try {
				statement.setString(4, SdeptText.getText());
			} catch (SQLException e5) {
				System.out.println("失败！\n");
				e5.printStackTrace();
			}
			try {
				statement.setString(5, suseText.getText());
			} catch (SQLException e6) {
				System.out.println("失败！\n");
				e6.printStackTrace();
			}
			try {
				statement.setString(6, timeText.getText());
			} catch (SQLException e7) {
				System.out.println("失败！\n");
				e7.printStackTrace();
			}
			try {
				int c = 1;
				statement.setLong(7, c);
			} catch (SQLException e7) {
				System.out.println("失败！\n");
				e7.printStackTrace();
			}

			try {
				statement.executeUpdate();
			} catch (SQLException e1) {
				System.out.println("失败！\n");
				e1.printStackTrace();
			}
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection
						.prepareStatement("select *from ES " + "where Sname" + "=" + "'" + users.getName() + "'");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		// ***二级防疫部门***
		if (e.getSource() == add && type == 2) {// 二级防疫部门
			JOptionPane.showMessageDialog(null, "插入已成功！", "警告", JOptionPane.WARNING_MESSAGE);
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("insert into dbo.ES values(?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println("插入失败！\n");
				e1.printStackTrace();
			}
			try {
				statement.setString(1, snoText.getText());
			} catch (SQLException e2) {
				System.out.println("失败！\n");
				e2.printStackTrace();
			}
			try {
				statement.setString(2, nameText.getText());
			} catch (SQLException e3) {
				System.out.println("失败！\n");
				e3.printStackTrace();
			}
			try {
				statement.setString(3, esText.getText());
			} catch (SQLException e4) {
				System.out.println("失败！\n");
				e4.printStackTrace();
			}
			try {
				statement.setString(4, SdeptText.getText());
			} catch (SQLException e5) {
				System.out.println("失败！\n");
				e5.printStackTrace();
			}
			try {
				statement.setString(5, suseText.getText());
			} catch (SQLException e6) {
				System.out.println("失败！\n");
				e6.printStackTrace();
			}
			try {
				statement.setString(6, timeText.getText());
			} catch (SQLException e7) {
				System.out.println("失败！\n");
				e7.printStackTrace();
			}
			try {
				int c = 2;
				statement.setLong(7, c);
			} catch (SQLException e7) {
				System.out.println("失败！\n");
				e7.printStackTrace();
			}

			try {
				statement.executeUpdate();
			} catch (SQLException e1) {
				System.out.println("失败！\n");
				e1.printStackTrace();
			}
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection
						.prepareStatement("select *from ES " + "where Sname" + "=" + "'" + users.getName() + "'");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		// *** 教职工 ***
		if (e.getSource() == add && type == 5) {// 教职工
			JOptionPane.showMessageDialog(null, "插入已成功！", "警告", JOptionPane.WARNING_MESSAGE);
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("insert into dbo.ES values(?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println("插入失败！\n");
				e1.printStackTrace();
			}
			try {
				statement.setString(1, snoText.getText());
			} catch (SQLException e2) {
				System.out.println("失败！\n");
				e2.printStackTrace();
			}
			try {
				statement.setString(2, nameText.getText());
			} catch (SQLException e3) {
				System.out.println("失败！\n");
				e3.printStackTrace();
			}
			try {
				statement.setString(3, esText.getText());
			} catch (SQLException e4) {
				System.out.println("失败！\n");
				e4.printStackTrace();
			}
			try {
				statement.setString(4, SdeptText.getText());
			} catch (SQLException e5) {
				System.out.println("失败！\n");
				e5.printStackTrace();
			}
			try {
				statement.setString(5, suseText.getText());
			} catch (SQLException e6) {
				System.out.println("失败！\n");
				e6.printStackTrace();
			}
			try {
				statement.setString(6, timeText.getText());
			} catch (SQLException e7) {
				System.out.println("失败！\n");
				e7.printStackTrace();
			}
			try {
				int c = 2;
				statement.setLong(7, c);
			} catch (SQLException e7) {
				System.out.println("失败！\n");
				e7.printStackTrace();
			}

			try {
				statement.executeUpdate();
			} catch (SQLException e1) {
				System.out.println("失败！\n");
				e1.printStackTrace();
			}
			try {
				PreparedStatement state;
				ResultSet resultSet;
				state = connection
						.prepareStatement("select *from ES " + "where Sname" + "=" + "'" + users.getName() + "'");
				resultSet = state.executeQuery();
				while (mm.getRowCount() > 0) {// 把表格进行刷新，下次显示的时候重头开始显示
					// System.out.println(model.getRowCount());
					mm.removeRow(mm.getRowCount() - 1);
				}
				while (resultSet.next()) {
					String Sno = resultSet.getString(1);
					String Sname = resultSet.getString(2);
					String Ssex = resultSet.getString(3);
					String Sdept = resultSet.getString(4);
					String DDno = resultSet.getString(5);
					String Scheckin = resultSet.getString(6);
					String[] data = { Sno, Sname, Ssex, Sdept, DDno, Scheckin };
					mm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

}
