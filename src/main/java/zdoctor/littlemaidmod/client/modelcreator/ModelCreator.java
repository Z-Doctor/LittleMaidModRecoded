package zdoctor.littlemaidmod.client.modelcreator;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zdoctor.lazylibrary.util.FileUtil;
import zdoctor.littlemaidmod.client.model.CustomModelBase;

public class ModelCreator {

	protected static ModelCreator instance;

	protected boolean successful = false;

	protected File CONFIG_FOLDER;
	protected File MODEL_FOLDER;
	protected File SOUND_FOLDER;

	protected List<File> modelFileList;
	protected List<File> soundFileList;

	protected List<CustomModelBase> modelList;

	public ModelCreator(FMLPreInitializationEvent e) {
		this(new File(e.getModConfigurationDirectory(), "LittleMaidConfig"));
	}

	public ModelCreator(File configDir) {
		CONFIG_FOLDER = configDir;
		if (!CONFIG_FOLDER.exists() && !CONFIG_FOLDER.mkdir()) {
			successful = false;
			return;
		}

		initConfig();
		loadData();

		successful = true;
		if (instance == null)
			instance = this;
	}

	protected void initConfig() {
		MODEL_FOLDER = new File(CONFIG_FOLDER, "Models");
		SOUND_FOLDER = new File(CONFIG_FOLDER, "Sounds");

		if ((!MODEL_FOLDER.exists() && !MODEL_FOLDER.mkdir()) || (!SOUND_FOLDER.exists() && !SOUND_FOLDER.mkdir())) {
			successful = false;
			return;
		}
	}

	protected void loadData() {
		modelFileList = FileUtil.getDirList(MODEL_FOLDER, new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getPath().endsWith(".json");
			}
		});

		modelFileList.forEach(modelFile -> {
			// try {
			// // CustomModelRegistry.registerModel(modelFile);
			// } catch (IOException e) {
			// e.printStackTrace();
			// FMLLog.log.catching(e);
			// }
		});

		soundFileList = FileUtil.getDirList(MODEL_FOLDER, new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getPath().endsWith(".json");
			}
		});
	}

	public List<File> getModelList() {
		return modelFileList;
	}

	public List<File> getSoundList() {
		return soundFileList;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public static ModelCreator getInstance() {
		return instance;
	}

}
