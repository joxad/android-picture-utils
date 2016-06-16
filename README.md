# PictureUtils
Use this library to take picture from gallery or phones.

# Version:
1.0.0


# Installation

Top Level Gradle :
```groovy
allprojects {
    repositories {
        maven {
            url "http://dl.bintray.com/joxad/maven"
        }
    }
}
```

Project's build.gradle

```groovy
dependencies {
    compile "com.joxad.pictureutils.lib:$currentVersion"
}
```

# Utilisation

For now in the activity where you need it 

## Create PictureUtils
 
```groovy
 new PictureUtils.Builder().context(this).build();
     
 ```

To handle the result you need to add :
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PictureUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
```

## Get Image
 
  
```java
 PictureUtils.setListener(new PictureUtils.Listener() {
            @Override
            public void onImageSelected(Uri fileUri) {
                imageView.setImageURI(fileUri);
            }
        });
           
```

## Permissions
```java


    public void onClick(View view) {
        PictureUtils.askPermission(new PictureUtils.IPermission() {
            @Override
            public void onPermissionAccepted() {
                PictureUtils.showDialogPicker("Title", "Take Photo", "From Gallery", "Cancel");
            }

            @Override
            public void onPermissionDenied() {

                Log.d(TAG, "Denied permission");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PictureUtils.onRequestPermissionsResults(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

```


#Contribution 

Feel free to make pull request in order to make some evolutions on it.


#Licence

The MIT License (MIT)
Copyright (c) 2016 Jocelyn David

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
