import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * class description here
 * author ling_cx
 * date 2017/10/20.
 */

public class PluginUtils {
	private Project mProject;
	private String mAuthorName;
	public PluginUtils(Project project,String authorName) {
		mProject = project;
		mAuthorName = authorName;
	}

	/**
	 * 刷新项目
	 * @param e
	 */

	public void refreshProject(AnActionEvent e) {
		e.getProject().getBaseDir().refresh(false, true);
	}

	public void createFile(String templateName,String createFolderName){
		String createFileName = "";
		if(templateName.contains("/")){
			String code[] = templateName.split("/");
			createFileName = code[code.length - 1].split("\\.")[0]+".java";
		}else{
			createFileName = templateName.split("\\.")[0]+".java";
		}
		createFile(templateName,createFolderName,createFileName);
	}

	public void createFile(String templateName,String createFolderName,String createFileName){
		String content = "";
		String apiPath = "";
		if(createFolderName.equals("project")){
			apiPath = mProject.getBasePath();
		}else if(createFolderName.equals("app")){
			apiPath = mProject.getBasePath() + "/app";
		}else{
			apiPath = getAppPath() + createFolderName +"/";
		}
		if (!new File(apiPath + createFileName).exists()){
			content = ReadTemplateFile(templateName);
			content = dealTemplateContent(content,createFolderName);
			writeToFile(content, apiPath, createFileName);
		}
	}

	/**
	 * 获取包名文件路径
	 * @return
	 */
	private String getAppPath(){
		String packagePath = getPackageName().replace(".", "/");
		String appPath = mProject.getBasePath() + "/app/src/main/java/" + packagePath + "/";
		return appPath;
	}

	/**
	 * 替换模板中字符
	 * @param content
	 * @return
	 */
	private String dealTemplateContent(String content,String module) {
		if (content.contains("$packageName")){
			content = content.replace("$packageName", getPackageName() + "." + module.toLowerCase().replace("/","."));
		}
		if (content.contains("$package")){
			content = content.replace("$package", getPackageName());
		}
		content = content.replace("$author", mAuthorName);
		content = content.replace("$date", getDate());
		return content;
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public String getDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 读取模板文件中的字符内容
	 * @param fileName 模板文件名
	 * @return
	 */
	private String ReadTemplateFile(String fileName) {
		InputStream in = null;
		in = this.getClass().getResourceAsStream("/template/" + fileName);
		String content = "";
		try {
			content = new String(readStream(in),"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	private byte[] readStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		try {
			while ((len = inputStream.read(buffer)) != -1){
				outputStream.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			outputStream.close();
			inputStream.close();
		}
		return outputStream.toByteArray();
	}

	/**
	 * 生成
	 * @param content 类中的内容
	 * @param classPath 类文件路径
	 * @param className 类文件名称
	 */
	private void writeToFile(String content, String classPath, String className) {
		try {
			File floder = new File(classPath);
			if (!floder.exists()){
				floder.mkdirs();
			}
			File file = new File(classPath + "/" + className);
			if (!file.exists()) {
				file.createNewFile();
			}else{//文件存在先清空内容
				FileWriter fileWriter =new FileWriter(file);
				fileWriter.write("");
				fileWriter.flush();
				fileWriter.close();
			}
			OutputStreamWriter filerWriter = new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile(),true),"UTF-8");
			BufferedWriter bw = new BufferedWriter(filerWriter);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 从AndroidManifest.xml文件中获取当前app的包名
	 * @return
	 */
	private String getPackageName() {
		String package_name = "";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(mProject.getBasePath() + "/app/src/main/AndroidManifest.xml");

			NodeList nodeList = doc.getElementsByTagName("manifest");
			for (int i = 0; i < nodeList.getLength(); i++){
				Node node = nodeList.item(i);
				Element element = (Element) node;
				package_name = element.getAttribute("package");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return package_name;
	}
}
