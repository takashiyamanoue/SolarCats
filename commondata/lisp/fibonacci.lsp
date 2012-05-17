'fibonacci
'
'    t.yamanoue, Apr, 2008
'
(defun fib (x)
    (cond ((= x 0) 0)
          ((= x 1) 1)
          (t (+ (fib (- x 1)) (fib (- x 2))))))
'
' an example of this function usage
'
'     (fib 5)
'     5
'     OK
'
(progn
 (for i 1 8 1
   (progn (print (fib i)))
  ))