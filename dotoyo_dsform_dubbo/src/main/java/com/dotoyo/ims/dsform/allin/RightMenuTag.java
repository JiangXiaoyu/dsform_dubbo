package com.dotoyo.ims.dsform.allin;

public class RightMenuTag {
	private String rightMenuTagId;
	private String Menutype;
	
	public String getRightMenuTagId() {
		return rightMenuTagId;
	}
	public void setRightMenuTagId(String rightMenuTagId) {
		this.rightMenuTagId = rightMenuTagId;
	}
	public String getMenutype() {
		return Menutype;
	}
	public void setMenutype(String menutype) {
		Menutype = menutype;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((rightMenuTagId == null) ? 0 : rightMenuTagId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RightMenuTag other = (RightMenuTag) obj;
		if (rightMenuTagId == null) {
			if (other.rightMenuTagId != null)
				return false;
		}else if(other == null){
			return false;
		}
		return rightMenuTagId.equals(other.getRightMenuTagId());
	}
	
}
