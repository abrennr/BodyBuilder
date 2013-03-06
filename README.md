# BodyBuilder

## Create Simple Structural Metadata in METS for Digitized Texts

Created: 2007-2013

This Documentation Date: February, 2013

[1. Overview][1]  

[2. Installation][2]  
  [2.1 Requirements][3]  
  [2.2 Distribution][4]  

[3. Using BodyBuilder][5] 
  [3.1 Preparing Texts for BodyBuilder][6]  
  [3.2 Starting BodyBuilder][7]  
  [3.3 Opening A Text][8]  
  [3.4 Navigating the Tree][9]  
  [3.5 Creating Native Pagination][10]  
  [3.6 Creating Structural Divisions][11]  
  [3.7 Editing Structural Divisions][12]  
  [3.8 Deleting Structural Divisions][13]  
  [3.9 Saving A Text][14]  
  [3.10 Closing BodyBuilder][15] 

[4. METS XML Output][16]  

[5. License ][17]  

<a name="1Overview"></a> 
## 1. Overview

BodyBuilder is a graphical desktop application that supports the creation of
simple XML structural metadata in METS [see <http://www.loc.gov/standards/mets/>]
for digitized texts.  

The software presents a tree-like
representation of the digitized text and its associated page images and supports the entry of
structural divisions (e.g., chapters, sections, articles) and native pagination.  The entered
metadata is saved as a simple METS XML document which may be further transformed as part of a
larger digital object creation workflow.

BodyBuilder is developed by the Digital Research Library at the University Library System,
University of Pittsburgh.

Questions, comments, or suggestions regarding BodyBuilder should be directed to the
Digital Research Library (<uls-digitalresearchlibrary@mail.pitt.edu>).

*History*: BodyBuilder evolved from an earlier tool used for capturing pagination and structural
divisions within TEI Lite `<BODY>` elements [see <http://www.tei-c.org/>] and that is how it got
its name.

<a name="2Installation"></a> 
## 2. Installation

The source can be compiled using the command-line java compiler `javac`, or built using your preferred
IDE.  If compiling by hand, the program can be started with the command

	java -cp /path/to/compiled/src edu.pitt.library.drl.bodybuilder.Main
	
In our local environment, we have built an executable jar file that is launched via Java Web Start;
this simplifies usage from any device in our environment.

<a name="21Requirements"></a> 
### 2.1 Requirements

BodyBuilder requires no libraries other than those included standard with Java.
It has been run with various Java Runtime Environments of version 1.5 or higher.
It should run on any operating system for which the Java Runtime Environment is available; however 
BodyBuilder has only been tested on Windows XP, Windows 7, and Mac OS X.
There are also some simple requirements to prepare data for use with BodyBuilder; these are covered in
section 3.1 "Preparing Texts for BodyBuilder" below.

<a name="22Distribution"></a> 
### 2.2 Distribution

BodyBuilder is distributed as source code, along with documentation and a sample text.

The project contains following files and subdirectories:

* `LICENSE.txt` - the license for this software
* `samples/` - a folder containing sample data to illustrate use of the software
* `src/` - the Java source code for the software.
* `README.md` - documentation for the software.


<a name="3UsingBodyBuilder"></a> 
## 3. Using BodyBuilder

<a name="31PreparingTextsforBodyBuilder"></a> 
### 3.1 Preparing Texts for BodyBuilder

To use a text in BodyBuilder, you will need to have a directory (representing the text) on an
accessible disk containing sequentially-named JPEG image files (representing the page images).
The image files must have the extension ".jpg" to be found by the software.   See the text in 
the "samples" folder for an example.

*Note*: In our workflow, the JPEG images used in BodyBuilder are created from TIFF images but are
used to speed the processing of large texts.  For this reason, the METS XML produced assumes a
file extension of ".tif" in the file URI.  (See section 4. "METS XML Output" below.) 

The images will display at their actual resolution in the application, so to fit an entire 
page on your screen you may want to constrain the long side of the image to 700-800 pixels 
(adjust to your display environment).  If a page image is larger than the display area, 
however, it will be viewable with scroll bars. 

The first time that a text is opened in BodyBuilder, the software will assemble a skeletal METS
XML document structure for the text, based on the page images found, and using the sequence of 
the page image file names.  There is no provision for manipulating or re-ordering the page image 
files, so any problems affecting page order should be fixed and the book re-prepared before 
continuing with the BodyBuilder process.  In this case, any existing BodyBuilder XML for the 
text will be incorrect and should be manually deleted before working with the corrected page 
images.

You can also optionally include OCR output along with the image files.  The software will look
for a corresponding text file with the same base file name as the page image, but with the
extension ".txt".  If found, the OCR text will be viewable in a tabbed-window along with the page
image for each page.  As with the page images, the OCR is not editable, but serves as a reference
to the source page.  It may also be useful to cut-and-paste text (such as author names) from the
OCR into the metadata for structural divisions.

<a name="32StartingBodyBuilder"></a> 
### 3.2 Starting BodyBuilder

Starting BodyBuilder depends on how it has been built, described in Section 2. "Installation" above.
After launching, the application will open a window that scales to available screen dimensions. The
main window is a split screen, with an area for the document tree view on the left, and an area for
the page images (or text) on the right.  Both areas will be empty until a text is loaded.

<a name="33OpeningAText"></a> 
### 3.3 Opening A Text

To open a text, select `File -> Open Text` from the menu.  You will be presented with a file chooser
appropriate for your operating system.  Navigate to *select* (but not *into*) the directory
containing your text's page images.  Click "Open" from the file chooser dialog. 

The text should open in the main BodyBuilder window.  If no XML has been created with BodyBuilder
already, a tree full of blank nodes (one for each page) will be created.  If an existing `<identifier>.mets.xml`
file is found in the directory, BodyBuilder will open the existing structure for further editing.

<a name="34NavigatingtheTree"></a> 
### 3.4 Navigating the Tree

You can navigate the tree using the mouse or the keyboard. Clicking on a node of the tree will
select it, and if the node is a page, the corresponding page image should appear.  The up and down
arrow keys also allow you to move up and down through the nodes. The right and left arrow keys will
open and close parent nodes, but note that the entire tree will be fully re-expanded any time a
change to its structure is made. 

<a name="35CreatingNativePagination"></a> 
### 3.5 Creating Native Pagination

Page nodes in the tree can be given numbers to capture the native pagination as printed in the
physical book.   By default, a new text in BodyBuilder will open with no page numbers.  Pages are
then numbered as they are selected in the tree.  The first page will always initially be named
`unum` (for "unnumbered"), until re-named.  One a page is named in a format that BodyBuilder
recognizes, it will automatically name subsequently selected unnamed pages according to the pattern.
This makes it very fast to number through a book (using the arrow keys) as long as the native
pagination stays consistent.

The patterns recognized, and their increments are:

*   4-digit number (e.g. `0125`) - next page will be incremented numerically
*   3-digit number with last character alphabetic (e.g. `001a`) - next page will be incremented
    alphabetically
*   first character "r" (for "roman numeral" followed by three digits (e.g. `r012`) - next page will be
    incremented numerically
*   `unum` for an unnumbered page - next page will also be `unum`

Page numbers must contain 4 alpha-numeric characters or else will not be entered by the software.

To change a page's number:

*   Select the page's node in the tree.
*   Use your operating system's method for renaming (usually a slow double click on the node, or a keyboard
    shortcut -- on Windows, it is the `F2` key).
*   Enter the page number directly into the node, and then `Enter` to save the name.
*   If the next pages follow the pattern and are not yet named, just click the down arrow key to auto-name
    the following pages.

Sometimes you may need to *change* a long sequence of pages that are already numbered, but you would like to use
the auto-numbering (which usually only works with unnumbered pages).  In this case, you can select the
"overwrite page numbers" check box just below the tree to perform a new auto-numbering on already named pages.

If you would like to auto-number all of the pages within a text without needing to scroll
through each one individually, you can do so by clicking the button labeled `AUTO #` below the document tree.

<a name="36CreatingStructuralDivisions"></a> 
### 3.6 Creating Structural Divisions

Structural divisions are inserted to represent divisions in the physical text and contain associated
metadata. Currently BodyBuilder supports only simple division metadata: a title for the division.
 Previous versions of BodyBuilder, prior to its using
the METS schema, allowed for extended division metadata including page range and authors.  In METS this
more detailed metadata should be stored in a separate `<dmdSec>`, using a separate descriptive standard
(e.g., MODS).  Although this functionality could be added to BodyBuilder, it is not currently available.
For more information on the METS XML that is created when adding divisions, see section 4, "METS XML Output" below.

Divisions can be nested into a hierarchy (e.g., a division representing a "section" contains "chapters",
which contain "articles").   BodyBuilder should support creating divisions in any
order -- in other words, you should be able to make the subdivisions first, and then enclose them in a
containing division -- and all divisions should nest properly.

To create a division:

*   Highlight all pages (including any subdivisions) in the new division using the shift key and clicking
   from the first to the last page.  It is important (in the creation of the XML) that the selection be in
   order, and without gaps.  If either of these conditions are found, the software should notify you and
   refuse to create the new division.
*   Next, click the `[+DIV]` button below the tree, or the "insert" key on the keyboard.
*   A form will appear to capture the division's metadata.  Here you should enter the title of the division.
*   When you have completed the data entry, click the `OK` button on the form, and the new division should
   appear in the tree.  Any contained subdivisions should have been moved down a level in the document tree
   to reflect their new position.

<a name="37EditingStructuralDivisions"></a> 
### 3.7 Editing Structural Divisions

Editing a structural division allows you to change or edit the metadata associated with the division.
However, it cannot change the position of the division within the text (to do this, you must delete the
division and re-create it again).

To edit a structural division:

*   Click on the division node on the tree (it must be the DIV node, not any of the metadata nodes whose
content you may intend to edit).
*   Click the `[EDIT Div]` button below the tree.
*   The same form used when creating the division will appear.  Make edits as necessary.
*   Click the `OK` button on the form to save the edit.


<a name="38DeletingStructuralDivisions"></a> 
### 3.8 Deleting Structural Divisions

Deleting a structural division will move all pages and subdivisions within back up a level in the text's
tree hierarchy.  To delete a division: 

*   Click the division node in the tree.
*   Click the `[–DIV]` button below the tree, or the `Delete` key on the keyboard.
*   Confirm the deletion.

<a name="39SavingAText"></a> 
### 3.9 Saving A Text

Saving a text writes the document structure to disk as an XML file.  The file is named `<identifier>.mets.xml`, 
and is written in the same directory that was opened to find the page image files.

To save the text, choose `File -> Save` from the menu.

<a name="310ClosingBodyBuilder"></a> 
### 3.10 Closing BodyBuilder

To close BodyBuilder, either select `File -> Quit`, or close the application window in the standard way.  If you
have an open text, and it has been modified since the last save, you will be prompted to save before quitting.

<a name="4XMLOutput"></a> 
## 4. METS XML Output

BodyBuilder creates intentionally simple XML, based on the minimal requirements for a METS instance, to permit
easy further manipulation as needed.

The XML output starts with an XML declaration, and then the opening of a `<mets>` element:
 
    <?xml version="1.0" encoding="UTF-8"?>
      <mets xmlns="http://www.loc.gov/METS/" xmlns:mets="http://www.loc.gov/METS/" xmlns:xlink="http://www.w3.org/1999/xlink">`
 
Pages are represented first with a METS `<fileSec>` and `<fileGrp>` element:

    <mets:fileSec>
      <mets:fileGrp>
        <mets:file ID="fid0001" MIMETYPE="image/tiff" SEQ="0001">
          <mets:FLocat LOCTYPE="URL" xlink:href="0001.tif" /> 
        </mets:file>
        <mets:file ID="fid0002" MIMETYPE="image/tiff" SEQ="0002">
          <mets:FLocat LOCTYPE="URL" xlink:href="0002.tif" />
        </mets:file>
        ...  

*Note:* the URL for each file is relative and assumes that the file extension is `.tif` (although jpegs are
used for display in BodyBuilder).

The `<fileSec>` is followed by a `<structMap>` to represent the document structure.  The @TYPE attribute is set to
"mixed", to indicated that the `<structMap>` combines physical page views with some logical document structure.
On first loading a text, BodyBuilder will create a `<div TYPE="volume">` inside the `<structMap>`.  It will then
create a `<div TYPE="page">` for each page of the text inside the "volume" `<div>`.  
The page `<div>` elements have a @LABEL attribute, which stores the native pagination as entered into BodyBuilder.
They also enclose a `<fptr>` element pointing to the corresponding page in the `<fileSec>` above.

Example:

    <mets:div LABEL="unum" TYPE="page">
      <mets:fptr FILEID="fid0001" /> 
    </mets:div>`

Divisions entered in BodyBuilder are represented with corresponding `<div TYPE="section>` elements.  The division's
title is represented in the @LABEL attribute.  Although the divisions may represent a variety of types (e.g., 
"chapter", "index", "part"), this simple implementation sets the type of every division to "section".

Example:
 
    <mets:div LABEL="1865 Map of Greensburg" TYPE="section">

Finally, the document closes with the `</mets>` tag.

*Note:* BodyBuilder does not perform validation of its XML against the METS schema

<a name="5Licensenbsp"></a> 
## 5. License 

	Copyright (c) 2007-2013 University of Pittsburgh
	
	Permission is hereby granted, free of charge, to any person obtaining a copy of this
	software and associated documentation files (the "Software"), to deal in the Software
	without restriction, including without limitation the rights to use, copy, modify, 
	merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
	permit persons to whom the Software is furnished to do so, subject to the following
	conditions:
	
	The above copyright notice and this permission notice shall be included in all copies
	or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
	INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
	PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
	HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
	CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
	OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 [1]: #1Overview
 [2]: #2Installation
 [3]: #21Requirements
 [4]: #22Distribution
 [5]: #3UsingBodyBuilder
 [6]: #31PreparingTextsforBodyBuilder
 [7]: #32StartingBodyBuilder
 [8]: #33OpeningAText
 [9]: #34NavigatingtheTree
 [10]: #35CreatingNativePagination
 [11]: #36CreatingStructuralDivisions
 [12]: #37EditingStructuralDivisions
 [13]: #38DeletingStructuralDivisions
 [14]: #39SavingAText
 [15]: #310ClosingBodyBuilder
 [16]: #4XMLOutput
 [17]: #5Licensenbsp