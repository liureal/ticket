package org.ticket.vo;

import java.util.List;

public class TicketBody {
	private String matchDate;
	private List<TicketInfo> infoList;
	public String getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}
	public List<TicketInfo> getInfoList() {
		return infoList;
	}
	public void setInfoList(List<TicketInfo> infoList) {
		this.infoList = infoList;
	}
	
}
