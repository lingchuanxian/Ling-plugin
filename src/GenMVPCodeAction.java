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
public class GenMVPCodeAction extends AnAction {

	private Project mProject;
	//包名
	private String mAuthorName = "";

	@Override
	public void actionPerformed(AnActionEvent event) {
		mProject = event.getData(PlatformDataKeys.PROJECT);
		init();
	}


	/**
	 * 初始化Dialog
	 */
	private void init(){
		CreateNewProjectDialog mDialog = new CreateNewProjectDialog(new CreateNewProjectDialog.DialogCallBack() {
			@Override
			public void ok(CreateNewProjectDialog dialog, String authorName) {
				mAuthorName = authorName;
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
	}

}
