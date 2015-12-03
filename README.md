#PoorEdit

Rich Text Editor for Android

#Status
WIP.

Buggy but almost there!

#Screenshots
I'm tring to make it look & feel like you are still in `Evernote`

<img src='art/shot1.png' width='300'/>
<br />
<img src='art/shot2.png' width='300'/>

#Features
* Basic text editing
* Bold
* Italic
* Images
* File attachments
* Todo lists
* Lists
* JSON export
* JSON import

#Usage
This project is still working in progress and I do NOT recommend using it in production env.

After check out this repo, you will get a test project and `PoorEdit` widget is located in `pooredit` folder. Import this folder as module in Android Studio.

Put below code in your layout xml file.

```xml
<sun.bob.pooredit.PoorEdit
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/id_poor_edit"/>
```

And in the Activity which contains this widget, you may need to override `onActivityResult` function as below.

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data){
    poorEdit.onActivityResult(requestCode, resultCode, data);
    //Write your code below.
}
```

The reason for that is `PoorEdit` need to start image picking or file picking activities and it will require picking result after those activities finished.

That's all you need to do to initialize a shinny tinny rich editor.

#Todos
* UI tweaks.
* Bug fix.
* Add voices.

#Why
To be honest I always thinking about writing a rich text editor on Android. 

There are lots of brilliant projects on GitHub.

But still, I wondering whether I can do it or not.

So here we are.

I take `Evernote`'s edit widget as a reference. And I have to say to writing a complex widget like that is so hard for just one guy.

So,

###ANY PULL REQUSTS IS WELCOME!
###THANKS IN ADVANCE!

#Credits
Icons from [icons8.com](https://icons8.com/)

#License

###Good Boy License

Please do whatever your mom would approve of.