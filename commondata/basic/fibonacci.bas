'fibonacci
'
'    t.yamanoue, May, 1999
'
def fib(x)=     if x=0 then 0
           else if x=1 then 1
           else fib(x-1)+fib(x-2)
'
' an example of this function usage
'
'     ?fib(5)
'     5
'     OK
for i=1 to 8
  ?fib(i)
next i
