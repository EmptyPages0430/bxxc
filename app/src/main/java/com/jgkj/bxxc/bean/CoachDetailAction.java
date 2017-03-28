package com.jgkj.bxxc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 获取教练所有信息
 */
public class CoachDetailAction implements Serializable {
	//返回码
	private int code;
	//result
	private List<Result> result;
	//返回信息
	private String reason;

	public String getReason() {
		return reason;
	}
	public int getCode() {
		return code;
	}
	public List<Result> getResult() {
		return result;
	}

	public class Result{
		//教练id
		private String cid;
		private String chexing;
		//校区
		private String faddress;
		//教练姓名
		private String coachname;
		//教练头像
		private String file;
		//班型
		private String class_type;
		//班型
		private String class_class;
		//车牌号
		private String number_plates;
		//信誉
		private String credit;
		//好评率
		private String praise;
		//通过率
		private String pass;
		//综合率
		private String zonghe;
		//所带学员数
		private String stunum;

		public String getClass_class() {
			return class_class;
		}

		public String getChexing() {
			return chexing;
		}

		public String getCid() {
			return cid;
		}

		public String getFaddress() {
			return faddress;
		}

		public String getCoachname() {
			return coachname;
		}

		public String getFile() {
			return file;
		}

		public String getClass_type() {
			return class_type;
		}

		public String getNumber_plates() {
			return number_plates;
		}

		public String getCredit() {
			return credit;
		}

		public String getPass() {
			return pass;
		}

		public String getPraise() {
			return praise;
		}

		public String getZonghe() {
			return zonghe;
		}

		public String getStunum() {
			return stunum;
		}

	}
}
