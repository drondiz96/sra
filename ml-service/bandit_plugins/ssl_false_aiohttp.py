"""
X101 – aiohttp.*(..., ssl=False)  ⇒  отключена проверка TLS-сертификата (CWE-295)
"""
import ast
from bandit.core import issue, test_properties as test

@test.test_id("X101")          # проставляет id + version
@test.checks("Call")           # маркер для AST-узлов Call
def aiohttp_ssl_false(context):
    if context.call_function_name not in {"get", "post", "request"}:
        return

    for kw in context.node.keywords:
        if kw.arg == "ssl" and isinstance(kw.value, ast.NameConstant) and kw.value.value is False:
            return issue.Issue(
                severity   = "HIGH",     # ← строки вместо констант
                confidence = "HIGH",
                cwe        = 295,
                text       = "aiohttp call with ssl=False disables certificate validation",
            )
