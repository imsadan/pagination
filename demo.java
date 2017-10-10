<!--以下ssm的基本-->
1)myabtis操作数据库
<!-- 根据分页数据start 和size查询数据 -->
<select id="findByPage" parameterType="Map" resultMap="BaseResultMap">
	select
	<include refid="Base_Column_List" />
	from news_list
	<if test="start!=null and size!=null">
		limit #{start},#{size}
	</if>
</select>

List<NewsList> findByPage(HashMap<String,Object> map);

2)pojo类
3)page
public class Page<T> {
	private int currPage;//当前页数
    private int pageSize;//每页显示的记录数
    private int totalCount;//总记录数
    private int totalPage;//总页数
    private List<T> lists;//每页的显示的数据
	....
	//按个人配置
}

4)service类及控制层
public Page<NewsList> findByPage(int currentPage) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		Page<NewsList> pages=new Page<>();
		//当前页面
		pages.setCurrPage(currentPage);
		//显示数据
		int pageSize=3;
		pages.setPageSize(pageSize);
		//总数
		int totalCount=newsListMapper.selectCount();
		pages.setTotalCount(totalCount);
		//总页数
		double tc=totalCount;
		Double number=Math.ceil(tc/pageSize);
		pages.setTotalPage(number.intValue());
		
		map.put("start",(currentPage-1)*pageSize);
		map.put("size",pages.getPageSize());
		//每页显示数据
		List<NewsList> list=newsListMapper.findByPage(map);
		pages.setLists(list);
		return pages;
	}

	@RequestMapping(value="/get/pageNews",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPageNews(@RequestParam(value="currentPage",defaultValue="1",required=false)
    Integer currentPage){
		Page<NewsList> page=newService.findByPage(currentPage);
		List<Page<NewsList>> list=new ArrayList<>();
		list.add(page);
		String s=JsonUtils.objectToJson(list);
		return s;
		
	}
