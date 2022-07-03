import com.wuweibi.module4j.SupperModuleimport com.wuweibi.module4j.module.ModuleContextimport org.marker.mushroom.beans.Menuimport org.marker.mushroom.ext.menu.MenuUtilsimport org.marker.mushroom.ext.plugin.PluginContextimport org.marker.mushroom.ext.plugin.Pluginlet/** * 模块 *  * @author marker *  */public class TestModuleActivatorImpl extends SupperModule {		/**	 * 启动模块	 * @param context	 * @param module	 * @throws Exception	 */	public void start(ModuleContext context ) throws Exception {				Class<?> clzz = require("GuestBookPluginlet");		if(null != clzz){			PluginContext pluginContext = PluginContext.getInstance();						pluginContext.put((Pluginlet)clzz.newInstance());						Menu menu = new Menu();			menu.setIcon("fa-comments-o");			menu.setName("留言管理");			menu.setUrl("/plugin/guestbook/list");			menu.setModuleId(module.getId());// 模块ID			menu.setSort(1000)			MenuUtils menuUtils = MenuUtils.getInstance();			menuUtils.builder("bb2bc5fb802544a2a4634013d2c19936", menu );		}else{            logger.error("插件加载失败", module.getConfig());		}        // 加载插件标签					} 	public void stop(ModuleContext context) throws Exception { 		MenuUtils menuUtils = MenuUtils.getInstance();		menuUtils.remove(this.id);		 	}}