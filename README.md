Real48 - Borland Delphi Real48 in Java
======================================

When writing a converter tool for a binary file format, I had problems 
reading some of the floating point numbers. After a little bit of research 
I found out that this very special format was used in programs written in 
Borland Pascal/Delphi. Since I couldn't find any Java class that's able to 
read this kind of number I wrote it myself.
As far as I remember these are basically reverse IEEE floating point 
numbers of six bytes. (It's been a while since I wrote this. See the PDF 
file by Richard Biffl for a detailed description of the format.)

References:

http://www.wotsit.org/list.asp?search=Borland+Pascal+Real&button=GO!
http://mail.python.org/pipermail/python-list/2001-December/117947.html
