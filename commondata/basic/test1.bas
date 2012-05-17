'factorial
'
'  t.yamanoue, May, 1999
'
def factorial(x)= if x=1 then 1
                  else x*factorial(x-1)
'
' an example of this function usage
'
'     ?factorial(5)
'     120
'     OK
'
for i=1 to 7
  ?factorial(i)
next i
