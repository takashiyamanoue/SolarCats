'Prime numbers
' by the Sieve of Eratosthenes
'
'    t.yamanoue, May, 1999
'
dim s
n=100
for i=1 to n:s(i)=1:next i
'
for i=2 to n
  for j=i to n/i:s(i*j)=0:next j
next i
'
for i=2 to n
  if s(i)=1 then ? ""+i+" is a prime number."
next i


