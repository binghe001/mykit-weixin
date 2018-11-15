package io.mykit.weixin.utils.thread;

import javax.management.relation.RoleUnresolved;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 任务执行工具
 * @author liuyazhuang
 *
 */
public class ExecutorUtils {
	
	private static volatile ExecutorService instance = null;
	static{
		instance = Executors.newFixedThreadPool(5);
	}
	
	/**
	 * 执行任务
	 * @param task
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("hiding")
	public static <T> List<T> executeCollector(Callable<List<T>> task) throws Exception{
		Future<List<T>> future = instance.submit(task);
		return future.get();
	}
	
	@SuppressWarnings("hiding")
	public static <T> T executeObject(Callable<T> task) throws Exception{
		Future<T> future = instance.submit(task);
		return future.get();
	}
	
	public static void executeThread(Runnable task){
		instance.execute(task);
	}

	public static void submitThread(Runnable task){
		instance.submit(task);
	}
	

	public static void shutdown(){
		if(instance != null){
			instance.shutdown();
			instance = null;
		}
	}
}
