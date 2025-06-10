"""
X102 – вывод переменных, похожих на SECRET / TOKEN / KEY  (CWE-532)
"""
import ast, re
from bandit.core import issue, test_properties as test

_RX = re.compile(r"(key|token|secret|password)", re.I)

@test.test_id("X102")
@test.checks("Call")
def print_secret(context):
    if context.call_function_name != "print":
        return

    for arg in context.node.args:
        if isinstance(arg, ast.Name) and _RX.search(arg.id):
            return _hit(context, f"variable '{arg.id}'")

    for kw in context.node.keywords or []:
        if kw.arg and _RX.search(kw.arg):
            return _hit(context, f"kwarg '{kw.arg}'")

def _hit(context, what):
    return issue.Issue(
        severity   = "MEDIUM",
        confidence = "MEDIUM",
        cwe        = 532,
        text       = f"Potential secret leak via {what}",
    )
