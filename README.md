# FancyEditText
Customized EditText component to use text fields in an interactive way

## Features
- Provides two icons within text field with following functionalities:
    - Can be used to clear the whole text
    - Show/hide usage of the password or hidden fields
## Known Issues
- Emoji enabling doesn't work, but disabled works for sure :)

## Usage
### Default EditText
~~~xml
<com.loodos.fancyedittext.FancyEditText
    android:id="@+id/name"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="10dp"
    android:hint="@string/name"
    android:maxLines="1"
    android:singleLine="true" />
~~~

### Default FancyEditText
It's possible to use it as its default by just adding `dynamicBackgroundEnabled="true"`  

~~~xml
<com.loodos.fancyedittext.FancyEditText
    android:id="@+id/password"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="10dp"
    android:hint="@string/prompt_password"
    android:imeActionId="6"
    android:imeActionLabel="@string/action_sign_in_short"
    android:imeOptions="actionUnspecified"
    android:inputType="textPassword"
    android:maxLines="1"
    android:singleLine="true"
    app:dynamicBackgroundEnabled="true" />
~~~

## Attributes
| Name          | Format          | Description   |
|:------------- |:---------------:| -------------:|
| clearEnabled             | boolean         | Enables the first action icon that clears the text on click with given/default clear icon drawable |
| emojiEnabled             | boolean         | Enables the usage of emojis within the text field (not working for now) |
| pwdIconEnabled           | boolean        | Enables the second action icon that shows/hides the text with given/default password icon drawable |
| showPwdIcon              | reference      | Drawable reference for showing password icon |
| hidePwdIcon              | reference      | Drawable reference for hiding password icon |
| dynamicBackgroundEnabled | boolean        | Enables the usage of dynamic background drawables for focused, error and success backgrounds |
| focusedBackground        | reference      | Drawable reference for text field's background when focused |
| errorBackground          | reference      | Drawable reference for text field's background when a type of error case occurs |
| successBackground        | reference      | Drawable reference for text field's background when a type of success case occurs |

## Default Icons
- [Show Password](https://material.io/icons/#ic_visibility)
- [Hide Password](https://material.io/icons/#ic_visibility_off)
- [Clear Text](https://material.io/icons/#ic_clear)

## Default Background Drawables
### selector\_rectangle\_default  

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_focused="false" android:drawable="@drawable/shape_rectangle_default" />
    <item android:state_focused="true" android:drawable="@drawable/shape_rectangle_default_focused" />
</selector>
~~~

#### shape\_rectangle\_default
![Default](https://image.ibb.co/nOjOSx/default.png)

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <stroke
        android:width="1dp"
        android:color="#CCCCCC" />
    <solid android:color="@android:color/transparent" />
</shape>
~~~

#### shape\_rectangle\_default\_focused
![Focused](https://image.ibb.co/e1Nz0H/focused.png)

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <stroke
        android:width="1dp"
        android:color="#00B0CA" />
    <solid android:color="@android:color/transparent" />
</shape>
~~~

### shape\_rectangle\_default\_error
![Errored](https://i.imgur.com/crQZHw2.png)

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">

    <stroke
        android:width="1dp"
        android:color="#E60000" />
    <solid android:color="@android:color/transparent" />
</shape>
~~~

### shape\_rectangle\_default\_success
![Succeed](https://image.ibb.co/jMMWEc/succeed.png)

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <stroke
        android:width="1dp"
        android:color="#428600" />
    <solid android:color="@android:color/transparent" />
</shape>
~~~

## Inspirations
XEditText https://github.com/woxingxiao/XEditText

## Contribute
Any issues and suggestion comments welcomed.

Note: For the README, please conform to the [standard-readme](https://github.com/RichardLitt/standard-readme) specification.

## License

MIT License

Copyright (c) 2018 Loodos Inf. Tech. (Canberk Ozcelik)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
