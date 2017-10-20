import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * class description here
 * author ling_cx
 * date 2017/10/19.
 */
public class GenFrameCodeAction extends AnAction {

	private Project mProject;
	private PluginUtils mPluginUtils;

	@Override
	public void actionPerformed(AnActionEvent event) {
		mProject = event.getData(PlatformDataKeys.PROJECT);
		init();
		mPluginUtils.refreshProject(event);
	}


	/**
	 * 初始化Dialog
	 */
	private void init(){
		CreateNewProjectDialog mDialog = new CreateNewProjectDialog(new CreateNewProjectDialog.DialogCallBack() {
			@Override
			public void ok(CreateNewProjectDialog dialog, String authorName) {
				mPluginUtils = new PluginUtils(mProject,authorName);
				createClassFiles();
				Messages.showInfoMessage(mProject,"KindAmdroidFrame Generate Success","Generate Result");
				dialog.setVisible(false);
			}
		});
		mDialog.setVisible(true);
	}

	/**
	 * 生成类文件
	 */
	private void createClassFiles() {
		//api
		mPluginUtils.createFile("api/ApiEngine.txt","api");
		mPluginUtils.createFile("api/ApiException.txt","api");
		mPluginUtils.createFile("api/ApiService.txt","api");
		//base
		mPluginUtils.createFile("base/BaseModel.txt","base");
		mPluginUtils.createFile("base/BaseView.txt","base");
		mPluginUtils.createFile("base/BasePresenter.txt","base");
		//bean
		mPluginUtils.createFile("bean/HttpResult.txt","bean");
		mPluginUtils.createFile("bean/MessageEvent.txt","bean");
		//di
		mPluginUtils.createFile("di/component/AppComponent.txt","di/component");
		mPluginUtils.createFile("di/module/AppModule.txt","di/module");
		mPluginUtils.createFile("di/scope/ActivityScope.txt","di/scope");
		mPluginUtils.createFile("di/scope/FragmentScope.txt","di/scope");
		//global
		mPluginUtils.createFile("global/Contants.txt","global");
		mPluginUtils.createFile("global/WBApplication.txt","global");
		mPluginUtils.createFile("global/ActivityManager.txt","global");
		mPluginUtils.createFile("global/RxFilter.txt","global");
		mPluginUtils.createFile("global/RetryWithDelay.txt","global");
		//widget
		mPluginUtils.createFile("widget/ToolBarSet.txt","widget");
		//gradle
		mPluginUtils.createFile("ProjectBuild.txt","project","build.gradle");
		mPluginUtils.createFile("config.txt","project","config.gradle");
		mPluginUtils.createFile("AppBuild.txt","app","build.gradle");
		//proguard-rules
		mPluginUtils.createFile("proguard-rules.txt","app","proguard-rules.pro");
	}
}
