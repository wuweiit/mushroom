package org.marker.mushroom.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



/**
 * 日志清理任务
 * @author marker
 * @version 1.0
 */
@Component
public class LogCleanTask {
	
	// 每过5秒执行一次
	@Scheduled(initialDelay=1000,fixedRate=999999999)
	public void run() { 
		System.out.println("启动日志清理任务。。。。。");
		
		
		
		
		
		
		
	} 
	
}