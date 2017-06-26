package org.marker.urlrewrite;


import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 根据Parameter对象自动处理的重写规则
 * 
 * @author marker
 * */
public final class RewriteRule {

    public int params = 0;
    // 参数名称表
    public String[] paramNames;

	//进站正则
	public Pattern inPattern;
	public String inResult;
	
	//出站正则
	public Pattern outPattern;
	public String outResult;
	
	public RewriteRule(Pattern inPattern, String inResult, Pattern outPattern,String outResult){
		this.inPattern = inPattern;
		this.inResult = inResult;
		this.outPattern = outPattern;
		this.outResult = outResult;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("InPattern: "+this.inPattern.toString()+"\n");
		sb.append("InUrl: "+this.inResult+"\n");
		sb.append("OutPattern: "+this.outPattern.toString()+"\n");
		sb.append("OutUrl: "+this.outResult+"\n");
		return sb.toString();
	}


    /**
     * 检查URl
     * @param url
     * @return
     */
    public boolean checkUrl(String url) {
        Map<String,String> params = UrlStringUtils.parseQueryString(url.replace("/cms?",""));

        Set<String> sets = params.keySet();
        for(String key : paramNames){
             if(!hasKey(sets, key)){
                 return false;
             }
        }
	    return true;
    }

    private boolean hasKey(Set<String> sets, String key){
        for(String k : sets){// 在请求参数里找到了继续
            if(key.equals(k)){
                return true;
            }
        }
        return false;
    }
}
