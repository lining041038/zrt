package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 数据地图组织机构关联关系实体类
 * @author lining
 */
@Entity
@Table(name="SYS_MAPREGION_UNIT_RELATE")
public class SysMapRegionRelate {
	@Id
	public String sid;
	public String regionsid;
	public String regionname;
	public String unitsid;
	public String unitname;
}
