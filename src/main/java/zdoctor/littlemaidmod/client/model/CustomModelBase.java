package zdoctor.littlemaidmod.client.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class CustomModelBase extends ModelBase {
	public static boolean CUSTOM_MAIDS_LOADED = false;

	protected String modelName;
	protected ResourceLocation modelSkin;
	protected String parent;

	public HashMap<String, ModelRenderer> boxMap = new HashMap<>();
	public HashMap<String, ArrayList<ModelRenderer>> parentMap = new HashMap<>();

	public CustomModelBase() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		boxMap.values().forEach(modelRender -> modelRender.render(scale));
	}

	public void defaultRender(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch, float scale) {

	}

	public void build() {

	}

	public void defaultBuild() {

	}

}
