import java.io.File

// A small script that replaces 1.15.2 genereated blockbench java models
// line by line to make them (mostly) compatable with 1.16 snapshots

val filename = "FireballModel"
val filepath = "C:\\Users\\micha\\Documents\\Minecraft Development\\Minecraft Assets\\model_conversion\\$filename.java"
val convertedClass = File(filepath).bufferedReader().use { it.readText() }
        .replace("custom_model", filename)
        .replace("setRotationPoint", "setPivot")
        .replace("addBox", "addCuboid")
        .replace("ModelRenderer", "ModelPart")
        .replace("public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){",
        "public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {")
        .replace("public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){",
        "public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {")
        .replace("render(matrixStack, buffer, packedLight, packedOverlay)",
        "render(matrices, vertices, light, overlay, red, green, blue, alpha)")
        .replace("\n" +
                "\tpublic void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {\n" +
                "\t\tmodelRenderer.rotateAngleX = x;\n" +
                "\t\tmodelRenderer.rotateAngleY = y;\n" +
                "\t\tmodelRenderer.rotateAngleZ = z;\n" +
                "\t}\n", "")
        .replace("// Exported for Minecraft version 1.15\n", "")
        .replace("// Paste this class into your mod and generate all required imports\n", "")

val classWithImports = "package net.barribob.maelstrom.model;\n\n" +
                "import net.minecraft.client.model.ModelPart;\n" +
                "import net.minecraft.client.render.VertexConsumer;\n" +
                "import net.minecraft.client.render.entity.model.EntityModel;\n" +
                "import net.minecraft.client.util.math.MatrixStack;\n" +
                "import net.minecraft.entity.Entity;\n" + convertedClass

print(classWithImports)