//package zdoctor.littlemaidmod.client.modelcreator;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.stream.JsonReader;
//
//import net.minecraft.client.model.ModelRenderer;
//import net.minecraft.util.ResourceLocation;
//import zdoctor.littlemaidmod.client.model.CustomModelBase;
//import zdoctor.littlemaidmod.client.model.ModelMaid;
//
//public class CustomModelRegistry {
//	public final static ArrayList<CustomModelBase> MAID_MODELS = new ArrayList<>();
//
//	public static void registerModel(File modelFile) throws IOException {
//		JsonObject jObject = new JsonParser().parse(new JsonReader(new FileReader(modelFile))).getAsJsonObject();
//
//		int version = jObject.get("version").getAsInt();
//		if (version == -1) {
//			parseCustom(jObject, version);
//			return;
//		}
//
//		if (version == 1) {
//			ModelMaid customMaidModel = new ModelMaid() {
//				@Override
//				public void build() {
//					modelName = jObject.get("name").getAsString();
//					// System.out.println("Parsing Model Name: " + modelName);
//					JsonObject texture = jObject.get("texture").getAsJsonObject();
//					JsonObject size = texture.get("size").getAsJsonObject();
//					textureWidth = size.get("width").getAsInt();
//					textureHeight = size.get("height").getAsInt();
//
//					String texturePath = texture.get("path").getAsString();
//					modelSkin = new ResourceLocation(texturePath);
//
//					JsonArray model = jObject.get("model").getAsJsonArray();
//					model.forEach(element -> {
//						ModelBuilder.addBox(element, this);
//
//					});
//					parentMap.forEach((parent, children) -> {
//						if (parent.equals(""))
//							return;
//						if (!boxMap.containsKey(parent))
//							return;
//						for (ModelRenderer child : children) {
//							boxMap.get(parent).addChild(child);
//						}
//					});
//				}
//			};
//			customMaidModel.build();
//			
//			MAID_MODELS.add(customMaidModel);
//			CustomModelBase.CUSTOM_MAIDS_LOADED = true;
//		}
//
//	}
//
//	public static void parseCustom(JsonObject jObject, int version) {
//
//	}
//
//	public static class ModelBuilder {
//
//		public static void addBox(JsonElement element, CustomModelBase modelBase) {
//			if (!element.isJsonObject())
//				return;
//			JsonObject modelBox = element.getAsJsonObject();
//			addBoxDefaults(modelBox);
//			System.out.println("Model:" + modelBox);
//			String boxName = modelBox.get("name").getAsString();
//			JsonObject texOffset = modelBox.get("tex-offset").getAsJsonObject();
//			JsonObject position = modelBox.get("position").getAsJsonObject();
//			JsonObject size = modelBox.get("size").getAsJsonObject();
//			JsonObject rotOffset = modelBox.get("rot-offset").getAsJsonObject();
//			boolean mirror = modelBox.get("mirror").getAsBoolean();
//			float scale = modelBox.get("scale").getAsFloat();
//			String parent = modelBox.get("parent").getAsString();
//			JsonArray children = modelBox.get("children").getAsJsonArray();
//			children.forEach(child -> {
//				if (!child.isJsonObject())
//					return;
//				JsonObject childObj = child.getAsJsonObject();
//				childObj.addProperty("parent", parent);
//				System.out.println("Created Child: " + childObj);
//			});
//
//			ModelRenderer model = new ModelRenderer(modelBase, texOffset.get("x").getAsInt(),
//					texOffset.get("y").getAsInt());
//			model.mirror = mirror;
//			model.addBox(position.get("x").getAsInt(), position.get("y").getAsInt(), position.get("z").getAsInt(),
//					size.get("x").getAsInt(), size.get("y").getAsInt(), size.get("z").getAsInt(), scale);
//			model.setRotationPoint(rotOffset.get("x").getAsInt(), rotOffset.get("y").getAsInt(),
//					rotOffset.get("z").getAsInt());
//
//			if (!modelBase.parentMap.containsKey(parent))
//				modelBase.parentMap.put(parent, new ArrayList());
//			modelBase.parentMap.get(parent).add(model);
//			modelBase.boxMap.put(boxName, model);
//
//		}
//
//		public static void addBoxDefaults(JsonObject modelBox) {
//			if (!modelBox.has("name"))
//				modelBox.addProperty("name", "");
//			if (!modelBox.has("tex-offset"))
//				modelBox.add("tex-offset", new JsonParser().parse("{ \"x\": 0, \"y\": 0 }"));
//			if (!modelBox.has("size"))
//				modelBox.add("size", new JsonParser().parse("{ \"x\": 0, \"y\": 0 }"));
//			if (!modelBox.has("rot-offset"))
//				modelBox.add("rot-offset", new JsonParser().parse("{ \"x\": 0, \"y\": 0, \"z\": 0 }"));
//			if (!modelBox.has("mirror"))
//				modelBox.addProperty("mirror", false);
//			if (!modelBox.has("scale"))
//				modelBox.addProperty("scale", 0.0F);
//			if (!modelBox.has("parent"))
//				modelBox.addProperty("parent", "");
//			if (!modelBox.has("children"))
//				modelBox.add("children", new JsonParser().parse("[]"));
//		}
//
//	}
//}
