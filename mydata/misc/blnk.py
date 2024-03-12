from pickle import TRUE
import tkinter as tk
import subprocess  
import shlex
import os
from datetime import datetime
import urllib.parse

#JavaScript
  
def anyStr(s):
    if (len(s) == 0):
        return " "
    else:
        return s

TEMPL = """
[//]: # (title)
## {title}

[//]: # (author)
{author}

[//]: # (dsc)
{description}

[//]: # (tags)
{tags}

[//]: # (img)
[<img src="img.png" width="150"/>](img.png)

[//]: # (url)
{url}

{content}

[//]: # (indexed=false)
[//]: # (changed=false)
[//]: # (date={date})
[//]: # (date +%F_%T)
"""

TEMPL_CONTENT = """
[//]: # (content)
{content}
"""

def saveForm():  
    time = datetime.now()
    #2022-02-15_12:41:41
    date = time.strftime("%Y-%m-%d_%H:%M:%S")
    ndird = time.strftime("%Y%m%d")
    hms = time.strftime("%H%M%S")
    ndir = ndird + hms
    outFile = ndir + "/index.md"

    contentText = txtContent.get("1.0", tk.END)

    contentStr = ""
    for content in contentText.split("\n"):
        if (len(content) > 0):
            cnt = "[{}]({})".format(content.replace('[','').replace(']',''), urllib.parse.quote(content))
            contentStr = contentStr + TEMPL_CONTENT.format(content=cnt)
    
    url=txtUrl.get()
    urlStr = url
    if (len(url) > 0):
        urlStr = "[{}]({})".format(url, url)
    
    tagsArr = [txtTags.get(idx) for idx in txtTags.curselection()]
    
    tagsArrLen = len(tagsArr)
    for i in range(tagsArrLen):
        tagsArr[i] = "- " + tagsArr[i] + "  "

    tagsStr = "\n".join(tagsArr)

    title=txtTitle.get()
    author=txtAuthor.get()
    dsc=txtDescription.get("1.0", tk.END)
    cmdName="dolphin"
    lblResult.configure(text=outFile)  
    #dolphin $ndir
    cmdarr = [
        cmdName,
        ndir
        ]
    print(cmdarr)
    ##subprocess.run(["ls", "-l"])
    templ = TEMPL
    print(outFile)
    doPrint = True;
    if (doPrint):
        os.makedirs(ndir, exist_ok=True)
        f = open(outFile, "w")
        f.write(templ.format(title = title, author = author, description = dsc, tags = tagsStr, url = urlStr, content = contentStr, date = date))
        f.close()
        subprocess.run(cmdarr)
    else:
        print(templ.format(title = title, author = author, description = dsc, tags = tagsStr, url = urlStr, content = contentStr, date = date))


erasers = []
def clearForm():
    for e in erasers:
        e.clear_text()

class Eraser():
    def __init__(self, entry, window):
        erasers.append(self)
        self.entry = entry
        self.button = tk.Button(window, text="x", command=self.clear_text, takefocus=0)

    def btn(self):
        return self.button

    def clear_text(self):
        self.entry.delete(0, 'end')

class EraserList(Eraser):
    def __init__(self, entry, window):
        super().__init__(entry, window)

    def clear_text(self):
        self.entry.selection_clear(0, tk.END)

class EraserText(Eraser):
    def __init__(self, entry, window):
        super().__init__(entry, window)

    def clear_text(self):
        self.entry.delete("1.0", tk.END)

window = tk.Tk()  
window.title("Создать описание ссылки или файла")  
window.geometry('1024x800')

wd = 40

entryCol = 2;
row = 0
lblUrl = tk.Label(window, text="Url")
lblUrl.grid(column=0, row=row)  
txtUrl = tk.Entry(window,width=wd)  
txtUrl.grid(sticky="w", column=entryCol, row=row)  
Eraser(txtUrl, window).btn().grid(column=1, row=row)  

row += 1
lblContent = tk.Label(window, text="Content filename")
lblContent.grid(column=0, row=row)  
txtContent = tk.Text(window, height=8)  
txtContent.grid(sticky="w", column=entryCol, row=row)  
EraserText(txtContent, window).btn().grid(column=1, row=row)  

row += 1

lblTags = tk.Label(window, text="Tags")
lblTags.grid(column=0, row=row)  
txtTags = tk.Listbox(window, width=wd, selectmode="extended")
txtTags.grid(sticky="w", column=entryCol, row=row)  
EraserList(txtTags, window).btn().grid(column=1, row=row)  

for i in ("css", 
    "dog", 
    "git", 
    "hacking",
    "html", 
    "Java", 
    "JavaScript", 
    "JavaSpring", 
    "maven", 
    "music", 
    "net", 
    "node", 
    "patterns", 
    "programming", 
    "React", 
    "TypeScript", 
    "география", 
    "музыка", 
    "физика", 
    "флейта", 
    "фортепиано", 
    "химия", 
    "школа", 
    "экономика"):
    txtTags.insert(tk.END, i)

row += 1
lblTitle = tk.Label(window, text="Title")
lblTitle.grid(column=0, row=row)  
txtTitle = tk.Entry(window,width=wd)  
txtTitle.grid(sticky="w", column=entryCol, row=row)  
Eraser(txtTitle, window).btn().grid(column=1, row=row)  

row += 1
lblAuthor = tk.Label(window, text="Author")
lblAuthor.grid(column=0, row=row)  
txtAuthor = tk.Entry(window,width=wd)  
txtAuthor.grid(sticky="w", column=entryCol, row=row)  
Eraser(txtAuthor, window).btn().grid(column=1, row=row)  

row += 1
lblDescription = tk.Label(window, text="Description")
lblDescription.grid(column=0, row=row)  
txtDescription = tk.Text(window, height=8)  
txtDescription.grid(sticky="w", column=entryCol, row=row)  
EraserText(txtDescription, window).btn().grid(column=1, row=row)  

row += 1
lblResult = tk.Label(window, text="Result")
lblResult.grid(column=1, row=row)  

row += 1
btnClear = tk.Button(window, text="Clear", command=clearForm)  
btnClear.grid(column=0, row=row)  
btnSave = tk.Button(window, text="Save", command=saveForm)  
btnSave.grid(column=1, row=row)  

window.mainloop()
