# CS2 Viewmodel - Fabric 1.21.11  (no Java / Gradle needed on your PC)

GitHub builds the jar for you:

1. Make a free account at github.com -> New repository (name it anything, Public or Private).
2. Click "uploading an existing file" and drag EVERYTHING from this unzipped folder into the browser
   (including the `.github` folder - if you can't see it, enable "show hidden files" first).
   Commit.
   - If `.github` won't upload: Add file -> Create new file, type  .github/workflows/build.yml
     as the name, paste the contents of that file from this zip, Commit.
3. Open the "Actions" tab. A "Build mod" run starts by itself (takes ~3-5 min). Wait for the green check.
4. Click the run -> scroll to "Artifacts" -> download `cs2viewmodel-jar` -> unzip -> you get cs2viewmodel-1.0.0.jar
5. Put that jar in .minecraft/mods together with Fabric Loader 0.18+ and Fabric API for 1.21.11.

If the run goes red, open it, copy the error text and send it to me.

In game: sword = butterfly knife, totem = flashbang, press F to inspect (rebind: Controls -> CS2 Viewmodel).
Tune hand positions in src/main/resources/assets/minecraft/models/item/*.json (display.firstperson_*).
