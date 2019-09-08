package controller.service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import util.LayuiData;
import Model.TBill;
import Model.VBill;
import business.impl.BillDaoImpl;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value = "bill")
public class BillController {
	/**
	 * 获取所有账单
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "getallbill")
	public void getallbill(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		BillDaoImpl bdao = new BillDaoImpl();
		List<VBill> list = bdao.getAllBill();
		// 回传json字符串
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		String userid = request.getParameter("user");
		String date = request.getParameter("yearmonth");
		System.out.print(userid + date);

		LayuiData laydata = new LayuiData();

		if (list != null) {
			laydata.code = LayuiData.SUCCESS;
			laydata.msg = "查询成功，共查出" + list.size() + "条记录";
			laydata.data = list;
		} else {
			laydata.code = LayuiData.ERRR;
			laydata.msg = "查询失败";
		}

		Writer out;
		try {
			out = response.getWriter();
			out.write(JSON.toJSONString(laydata));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "";
	}

	/**
	 * 根据用户id获取账单
	 * 
	 * @param request
	 * @param response
	 * @param userid
	 *            用户id
	 * @param model
	 */
	@RequestMapping(value = "getbillbyuser")
	public void getBillByUser(HttpServletRequest request,
			HttpServletResponse response, String userid, Model model) {
		BillDaoImpl bdao = new BillDaoImpl();
		List<VBill> list = bdao.getBillByUser(userid);
		// 回传json字符串
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		LayuiData laydata = new LayuiData();

		if (list != null) {
			laydata.code = LayuiData.SUCCESS;
			laydata.msg = "查询成功，共查出" + list.size() + "条记录";
			laydata.data = list;
		} else {
			laydata.code = LayuiData.ERRR;
			laydata.msg = "查询失败";
		}

		Writer out;
		try {
			out = response.getWriter();
			out.write(JSON.toJSONString(laydata));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "";
	}

	/**
	 * 添加账单
	 * 
	 * @param request
	 * @param response
	 * @param userid
	 *            用户id
	 * @param money
	 *            钱
	 * @param billType
	 *            账单类型（收入/支出）
	 * @param consumptionType
	 * @param remarks
	 *            备注
	 * @param createtime
	 *            时间
	 * 
	 * @param model
	 */
	@RequestMapping(value = "addbill")
	public void addBill(HttpServletRequest request,
			HttpServletResponse response, String userid, Double money,
			String billType, String consumptionType, String remarks,
			String createtime, Model model) {
		BillDaoImpl bdao = new BillDaoImpl();

		TBill bill = new TBill();
		bill.setUserId(userid);
		bill.setMoney(money);
		bill.setBillType(billType);
		bill.setConsumptionType(consumptionType);
		bill.setRemarks(remarks);
		bill.setCreateTime(createtime);

		// 回传json字符串
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		LayuiData laydata = new LayuiData();
		if (bdao.insertBill(bill) != 0) {
			laydata.msg = "账单添加成功";
		} else {
			laydata.msg = "账单添加失败";
		}

		Writer out;
		try {
			out = response.getWriter();
			out.write(JSON.toJSONString(laydata));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "";
	}

	/**
	 * 获取用户的 记账总天数，连续天数，记账总数量
	 * 
	 * @param request
	 * @param response
	 * @param userid
	 *            用户id
	 * @param model
	 */
	@RequestMapping(value = "getuserbillinfo")
	public void getUserBillInfo(HttpServletRequest request,
			HttpServletResponse response, String userid, Model model) {
		BillDaoImpl bdao = new BillDaoImpl();
		int allcount = bdao.getBillCountByUser(userid);
		Integer continueDays = bdao.getBillContinueDaysByUser(userid);
		Integer billDays = bdao.getBillDaysByUser(userid);
		// 回传json字符串
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		LayuiData laydata = new LayuiData();
		laydata.code = allcount;
		laydata.result = continueDays.toString();
		laydata.result2 = billDays.toString();
		laydata.msg = "code 是账单总数，result 是连续天数，result2是记账天数";
		Writer out;
		try {
			out = response.getWriter();
			out.write(JSON.toJSONString(laydata));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "";
	}

	/**
	 * 根据时间查询当天账单
	 * 
	 * @param request
	 * @param response
	 * @param time
	 *            时间
	 * @param model
	 */
	@RequestMapping(value = "getuserbillbytime")
	public void getUserBillByTime(HttpServletRequest request,
			HttpServletResponse response, String time, String userid,
			Model model) {
		BillDaoImpl bdao = new BillDaoImpl();
		String str = "from VBill where createTime like '" + time
				+ "%' and userid='" + userid + "'";
		System.out.println(str + "++++++" + time + userid);
		List<VBill> list = bdao.selectByPage(str, 1, 999999999);
		// 回传json字符串
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		LayuiData laydata = new LayuiData();
		laydata.data = list;
		laydata.msg = "当天账单数";
		Writer out;
		try {
			out = response.getWriter();
			out.write(JSON.toJSONString(laydata));
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "";
	}

	@RequestMapping(value = "getsumbymonth")
	public void getSumByMonth(String userid, HttpServletRequest request,
			HttpServletResponse response, String time, Model model) {
		BillDaoImpl bdao = new BillDaoImpl();

		// 回传json字符串
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		double in = bdao.getBillInByTime(userid, time);
		double out = bdao.getBillOutByTime(userid, time);
		double sum = in - out;
		LayuiData laydata = new LayuiData();
		if (sum >= 0) {
			laydata.code = LayuiData.SUCCESS;
			laydata.data = sum;
			laydata.msg = "结余";
		} else {
			laydata.code = LayuiData.ERRR;
			laydata.data = 0;
			laydata.msg = "结余";
		}
		Writer ptintout;
		try {
			ptintout = response.getWriter();
			ptintout.write(JSON.toJSONString(laydata));
			ptintout.flush();
			ptintout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "getyearbill")
	public void getYearBill(String userid, HttpServletRequest request,
			HttpServletResponse response, String year, Model model) {
		BillDaoImpl bdao = new BillDaoImpl();

		// 回传json字符串
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");

		List outlist = bdao.yearsBillOut(year, userid);
		List intlist = bdao.yearsBillInt(year, userid);

		for (int i = 0; i < outlist.size(); i++) {
			System.out.println(outlist.get(i).toString());
		}

		LayuiData laydata = new LayuiData();
		if (outlist != null && intlist != null) {
			laydata.code = LayuiData.SUCCESS;
			laydata.data = intlist;
			laydata.data1 = outlist;
			laydata.msg = "月账单";
		} else {
			laydata.code = LayuiData.ERRR;
			laydata.data = 0;
			laydata.msg = "月账单";
		}
		Writer ptintout;
		try {
			ptintout = response.getWriter();
			ptintout.write(JSON.toJSONString(laydata));
			ptintout.flush();
			ptintout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
