'   solving a linear system by
' Gaussian Elimination Method
'
'     by t.yamanoue, May, 1999
'
' dim a is the linear system
dim a
a(1,1)=1.0:a(1,2)=0.0:a(1,3)=0.0:a(1,4)=3.0
a(2,1)=0.0:a(2,2)=1.0:a(2,3)=0.0:a(2,4)=4.0
a(3,1)=0.0:a(3,2)=0.0:a(3,3)=1.0:a(3,4)=5.0
'
' n:size of the linear system
n=3
for i=1 to n
   p=a(i,i)
   for j=i to n+1:a(i,j)=a(i,j)/p:next j
   for k=1 to n
     if i<>k then
       {
          p=a(k,i)
          for j=i to n+1
             a(k,j)=a(k,j)-p*a(i,j)
          next j
       }
   next k
next i
'
' Output the solution
for i=1 to n
   ? a(i,n+1)
next i  